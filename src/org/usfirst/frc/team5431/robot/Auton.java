package org.usfirst.frc.team5431.robot;
import org.usfirst.frc.team5431.perception.Vision;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


class Auton{
	public static int state = 10;
	public static double startTime = 0;
	public static int previousState = -1;
	public static int driveState = 0;
	public static double futureTurn = 0;
	public static double travelledDistance = 0;
	public static int driveTurnState = 10;
	public static int visionState = 10;
	
	//Autonomous selection
	public static SendableChooser<Integer> autoChooser;
	public static boolean initialized = false;
	public static int autoSelected = 10;
	
	public static void init() {
		if(initialized) return;
		autoChooser = new SendableChooser<Integer>();
		autoChooser.addDefault("Middle", 10);
		autoChooser.addObject("Left", 20);
		autoChooser.addObject("Right", 30);
		autoChooser.addObject("LeftLong", 40);
		autoChooser.addObject("RightLong", 50);
		autoChooser.addObject("MiddleTwoGear", 60);
		autoChooser.addObject("StandStill", 70);
		autoChooser.addObject("DriveForward", 80);
		SmartDashboard.putData("Autonomous Mode Chooser", autoChooser);
		initialized = true;
	}
	
	public static void autoInit() {
		state = 10;
		driveTurnState = 10;
		visionState = 10;
		autoSelected = autoChooser.getSelected();
	}
	
	public static void periodic() {
		switch(autoSelected) {
		case 10:
			middle();
			break;
		case 20:
			left();
			break;
		case 30:
			right();
			break;
		case 40:
			leftLong();
			break;
		case 50:
			rightLong();
			break;
		case 60:
			twoGearMiddle();
			break;
		case 70:
			break;
		case 80:
			break;
		default:
			break;
		}
		
		Intake.updateFlipperPosition();
		
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
	}
	
	static boolean waited(int state, double seconds) {
		if(startTime == 0){
			startTime = Timer.getFPGATimestamp();
		}
		
		double currentTime = Timer.getFPGATimestamp();
		
		if(state != previousState) {
			startTime = currentTime;
		}
		
		previousState = state;
		
		if (currentTime-startTime >= seconds) {
			startTime = 0;
			return true;
		}
		return false;
	}

	static boolean travelled(double distance){
		if(distance < 0) {
			if(DriveBase.leftEncoder() <= distance || DriveBase.rightEncoder() <= distance) {
				DriveBase.resetEncoders();
				return true;
			} else {
				return false;
			}
		} else {	
			if (DriveBase.leftEncoder() >= distance || DriveBase.rightEncoder() >= distance) {
				SmartDashboard.putNumber("reached encoder distance", 1);
				DriveBase.resetEncoders();
				return true;
			} else {
				SmartDashboard.putNumber("reached encoder distance", 0);
				return false;
			}
		}
	}
	
	static boolean turned(double degrees){
//		if(!DriveBase.isPID()) {
//			DriveBase.drivePIDTurn(degrees);
//			double currentError = Math.abs(DriveBase.drivePID.getAvgError());
//			
//			if(currentError < 1.5) {
//				return true;
//			}
//			
//			return false;
//		}
//		
//		else
			if (degrees < 0){
				if (DriveBase.getYaw() <= degrees){
					return true;
				}
			}
			else 
				if (DriveBase.getYaw() >= degrees){
					return true;
				}
	
			return false;
	}
	
	static void stayStill(){
		DriveBase.driver(0, 0);
		if(DriveBase.isPID()) {
			DriveBase.disablePID();
			DriveBase.setDrivePIDSpeed(0);
		}
	}
	
	static void driveForward(double Power){
		if(!DriveBase.isPID()) {
			DriveBase.drivePIDForward(Power);
		}
	}
	
	static void driveBackward(double Power){
		if(!DriveBase.isPID()) {
			DriveBase.drivePIDBackward(Power);
		}
	}
	
	static void turnRight(double power){
		DriveBase.masterLeft.set(power);
		DriveBase.masterRight.set(power);
		//DriveBase.driver(-power, power);
	}
	
	static void turnLeft(double power){
		DriveBase.masterLeft.set(-power);
		DriveBase.masterRight.set(-power);
		//DriveBase.driver(power, -power);
	}
	
	static void visionPlacePeg() {
		switch(visionState) {
		case 10:
			Vision.setCameraPeg();
			Vision.setPegTargetMode();
			Vision.useAngleFromCamera();
			DriveBase.setPIDVision();
			//Vision.useAngleFromNavx();
			//DriveBase.setPIDNormal();
			if(waited(10, 0.25)) {
				visionState = 20;
			}
			break;
		case 20:
			driveForward(Constants.Auton.visionFindingPower);
			if(/*travelled(20) ||*/ Vision.foundTarget()) {
				if(waited(20, 0.5)) {
					stayStill();
					DriveBase.reset();
					Vision.useAngleFromCamera();
					DriveBase.setPIDVision();
					visionState = 30;
				}
			}
			break;
		case 30:
			driveForward(Constants.Auton.visionFoundPower);
			if(Vision.isOnTarget()) {
				stayStill();
				DriveBase.reset();
				DriveBase.setPIDNormal();
				Vision.useAngleFromNavx();
				visionState = 40;
			}
			break;
		case 40:
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				visionState = 50;
			}
			break;
		case 50:
			stayStill();
			if (waited(50, 0.5)){
				Intake.intakeRev();
				visionState = 60;
			}
			break;
		case 60:
			stayStill();
			if(waited(60, 0.5)) {
				Intake.intakeOff();
				visionState = 100;
			}
		default:
			break;
		};
	}
	
	static void driveAndTurn(double distance, double angle) {
		switch(driveTurnState) {
		case 10:
			stayStill();
			DriveBase.setPIDNormal();
			Vision.useAngleFromNavx();
			DriveBase.reset();
			driveTurnState = 20;
			break;
		case 20:
			if(distance >= 0) {
				driveForward(Constants.Auton.driveForwardPower);
			} else {
				driveBackward(Constants.Auton.driveBackwardPower);
			}
			
			Intake.intakeOff();
			Intake.flipperOff();
			if(travelled(distance)) {
				DriveBase.resetEncoders();
				driveTurnState = 30;
			}
			break;
		case 30:
			stayStill();
			if(waited(30, 0.5)) {
				DriveBase.reset();
				Vision.useAngleFromNavx();
				driveTurnState = 40;
			}
			break;
		case 40:
			if(angle < 0) {
				turnLeft(Constants.Auton.driveTurnPower);
			} else {
				turnRight(Constants.Auton.driveTurnPower);
			}
			
			if(turned(angle)) {
				stayStill();
				DriveBase.resetAHRS();
				driveTurnState = 100;
			}
			break;
		default:
			break;
		}
	}
	
	static void DriveForward(){
		switch(state)
		{
		case 10:
			driveForward(0.3);
			if(travelled(100)){
				state = 20;
			}
			break;
		case 20:
			stayStill();
			break;
		}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());

	}

	static void middle(){
		switch(state){
		case 10:
			driveForward(0.25);
			Intake.flipperBack();
			if(travelled(76)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			Intake.flipperUp();
			if(waited(70, .75)) {
				state = 71;
			}
			break;
		case 71:
			Intake.intakeRev();
			if(travelled(3.5)){
				state = 72;
				DriveBase.resetEncoders();
			}
			break;
		case 72:
			Intake.intakeRev();
			if(waited(72, 1)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			Intake.intakeOff();
		default:
			break;
			}
	}
	
	static void twoGearMiddle(){
		switch(state){
		case 10:
			driveForward(0.28);
			Intake.flipperBack();
			if(travelled(74)) {
				state = 20;
			}
			break;
		case 20:
			stayStill();
			Intake.flipperUp();
			if(waited(70, 0.75)) {
				state = 30;
			}
			break;
		case 30:
			Intake.intakeRev();
			if(travelled(3.5)){
				state = 40;
				DriveBase.resetEncoders();
			}
			break;
		case 40:
			Intake.intakeRev();
			if(waited(72, 0.25)) {
				state = 50;
			}
			break;
		case 50:
			driveBackward(0.4);
			if(travelled(-30)) {
				stayStill();
				state = 55;
			}
			break;
		case 55:
			turnLeft(0.3);
			if(turned(-80)) {
				stayStill();
				DriveBase.reset();
				Vision.setCameraGear();
				Vision.setGearTargetMode();
				Vision.useAngleFromCamera();
				DriveBase.setPIDVision();
				state = 60;
			}
			break;
		case 60:
			Intake.flipperDown();
			Intake.intakeOn();
			driveForward(Constants.Auton.visionFoundGearPower);
			if(Intake.isLimit()) {
				stayStill();
				Intake.intakeOff();
				Intake.flipperBack();
				travelledDistance = (DriveBase.leftEncoder() + DriveBase.rightEncoder()) / 2;
				SmartDashboard.putNumber("TravelledDistance", travelledDistance);
				DriveBase.reset();
				Vision.stopAllVision();
				state = 70;
			}
			break;
		case 70:
			driveBackward(0.45);
			if(travelled((-Math.abs(travelledDistance)) + 10)) {
				DriveBase.reset();
				stayStill();
				state = 75;
			}
			break;
		case 75:
			turnRight(0.3);
			if(turned(85)) {
				stayStill();
				DriveBase.reset();
//				Vision.setCameraPeg();
				state = 80;
			}
			break;
		case 80:
			System.out.println("VISION PLACE PEG STATE");
			visionPlacePeg();
			if(visionState == 100) {
				visionState = 10;
				state = 100;
			}
			break;
		case 100:
			stayStill();
			Intake.intakeOff();
		default:
			break;
			}
	}
	
	static void left() {
		switch(state) {
		case 10:
			driveAndTurn(68, 46);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 20;
			}
			break;
		case 20:
			visionPlacePeg();
			if(visionState == 100) {
				visionState = 10;
				state = 30;
			}
			break;
		case 30:
			stayStill();
			break;
		default:
			break;
		}
	}
	
	static void right() {
		switch(state) {
		case 10:
			driveAndTurn(68, -46);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 20;
			}
			break;
		case 20:
			visionPlacePeg();
			if(visionState == 100) {
				visionState = 10;
				state = 30;
			}
			break;
		case 30:
			stayStill();
			break;
		default:
			break;
		}
	}
	
	static void rightLong() {
		switch(state) {
		case 10:
			driveAndTurn(68, -46);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 20;
			}
			break;
		case 20:
			visionPlacePeg();
			if(visionState == 100) {
				stayStill();
				Vision.stopAllVision();
				DriveBase.reset();
				visionState = 10;
				state = 30;
			}
			break;
		case 30:
			driveAndTurn(-30, 50);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 40;
			}
			break;
		case 40:
			driveForward(0.8);
			if(travelled(280)) {
				state = 50;
			}
			break;
		case 50:
			stayStill();
			break;
		default:
			break;
		}
	}
	
	static void leftLong() {
		switch(state) {
		case 10:
			driveAndTurn(68, 46);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 20;
			}
			break;
		case 20:
			visionPlacePeg();
			if(visionState == 100) {
				stayStill();
				Vision.stopAllVision();
				DriveBase.reset();
				visionState = 10;
				state = 30;
			}
			break;
		case 30:
			driveAndTurn(-30, -50);
			if(driveTurnState == 100) {
				driveTurnState = 10;
				state = 40;
			}
			break;
		case 40:
			driveForward(0.8);
			if(travelled(280)) {
				state = 50;
			}
			break;
		case 50:
			stayStill();
			break;
		default:
			break;
		}
	}
	
	static void testGear() {
		switch(state) {
		case 10:
	    	stayStill();
	    	DriveBase.resetAHRS();
	    	DriveBase.resetEncoders();
	    	DriveBase.disablePID();
	    	DriveBase.setPIDVision();
	    	Vision.setCameraGear();
	    	Vision.setGearTargetMode();
	    	Vision.useAngleFromCamera();
	    	state = 11;
			break;
		case 11:
			Intake.flipperDown();
			Intake.intakeOn();
			driveForward(0.25);
			
			if(Intake.isLimit()) {
				Intake.intakeOff();
				Intake.flipperBack();
				state = 12;
			}
			break;
		case 12:
			stayStill();
			break;
		default:
			break;
		}
	}
	
	/*public static void blueRight() {
		switch(state)
		{
		case 10:
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			if(travelled(72.5))	//53
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			if (waited(30, 1.5)){
				state = 40;
			}
			break;
		case 40:
			turnLeft(0.2);
			if(turned(-60))
			{
				state = 50;
			}
			break;
		case 50:
			DriveBase.resetAHRS();
			
			stayStill();
			if (waited(50, 1)){
				DriveBase.resetEncoders();
				state = 60;
			}
			break;
			
		case 60:
			driveForward(0.27);
			if(travelled(16)) //32	//26
			{
				state = 70;
			}
			break;
		case 70:
			Intake.upAndOff();
			state = 80;
			break;
		case 80:
			Intake.intakeRev();
			stayStill();
			System.out.print("CASE 80");
		//	DriveBase.masterLeft.set(0);
		//	DriveBase.masterRight.set(0);
		//	Intake.intakeOff();
			Intake.flipperOff();
			if(waited(80,0.3)){
				state = 90;
			}
			break;
		case 90:
			DriveBase.resetEncoders();
			driveBackward(0.3);
			Timer.delay(1);
			state = 100;
			//Um . . .
			break;
		case 100:
			Intake.intakeOff();
			stayStill();
			break;
		}
	}
	
	static void blueLeft(){
		switch(state){
		case 10:
			driveForward(0.25);
			Intake.intakeOff();
			Intake.flipperOff();
			if(travelled(63)) {
				waited(10, 0.1);
				state = 30;
			}
			break;
		case 30:
			stayStill();
			if(waited(30, 2)) {
				state = 40;
			}
			break;
		case 40:
			turnRight(0.25);
			if(turned(52)) {
				DriveBase.resetAHRS();
				state = 50;
			}
			break;
		case 50:
			stayStill();
			if(waited(50, 0.5)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				state = 60;
			}
			break;
		case 60:
			driveForward(0.25);
			Intake.flipperUp();
			if(travelled(43)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			if(waited(70, 0.5)) {
				state = 71;
			}
			//Intake.upAndOff();
			//Timer.delay(1.5);
			break;
		case 71:
			driveForward(0.2);
			Intake.intakeRev();
			if(travelled(4)){
				state = 72;
			}
			break;
		case 72:
			stayStill();
			if(waited(72, 0.3)) {
				state = 73;
			}
			break;
		case 73:
			Intake.flipperDown();
			Intake.intakeRev();
			for(int i = 0; i < 550; i++) { 
				if(i > 250) DriveBase.driver(0.3, 0.3);
				if(i > 50) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			state = 80;
			break;
		case 80:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.intakeOff();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
	}
	
	static void twoGearMiddle(){
		Intake.updateFlipperPosition(); //To make all flipper setting functions work
		switch(state){
		case 10:
			driveForward(0.5);
			Intake.flipperBack();
//			Intake.flipperUp();
			waited(10, 0.1);
			if(travelled(74))
			{
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.placeGear();
			//Timer.delay(2);
			Intake.flipperUp();
			
			if(waited(70, 0.5)) {
				state = 71;
			}
			break;
		case 71:
//			driveForward(0.3);
			Intake.intakeRev();
			if(travelled(2)){
				state = 72; //SKIPPING
				DriveBase.resetEncoders();
			}
			break;
//		case 80:
//			stayStill();
//			Intake.intakeRev();
//			Intake.flipperOff();
//			Timer.delay(0.5);
//			Intake.intakeRev();
//			state = 90;
//		case 90:
//			driveForward(0.3);
//			//Intake.outGear();
//			Intake.intakeRev();
//			if(travelled(1)) {
//				state = 72;
//			}
		case 72:
			Intake.intakeRev();
//			Intake.flipperDown();
			for(int i = 0; i < 600; i++) { 
				if(i > 300) DriveBase.driver(0.3, 0.3);
				Timer.delay(1/100);
			}
			state = 80;
			DriveBase.resetEncoders();
			DriveBase.resetAHRS();
			break;
		case 80:
			Intake.intakeRev();
			Intake.flipperDown();
			driveForward(0.3);
			if(travelled(3)) {
				state = 100;
			}
		case 100:
			stayStill();
			Intake.flipperDown();
			//Intake.intakeOff();
//			Intake.flipperOff();
			Intake.intakeOff();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
}
	
	static void redMiddle(){
		Intake.updateFlipperPosition(); //To make all flipper setting functions work
		switch(state){
		case 10:
			driveForward(0.25);
			Intake.flipperBack();
//			Intake.flipperUp();
			waited(10, 0.1);
			if(travelled(76)) //76 inches
			{
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.placeGear();
			//Timer.delay(2);
			Intake.flipperUp();
			
			if(waited(70, .75)) {
				state = 71;
			}
			break;
		case 71:
//			driveForward(0.3);
			Intake.intakeRev();
			if(travelled(3.5)){
				state = 72; //SKIPPING
				DriveBase.resetEncoders();
			}
			break;
		case 72:
			Intake.intakeRev();
			if(waited(72, 1)) {
				state = 73;
			}
			break;
//		case 80:
//			stayStill();
//			Intake.intakeRev();
//			Intake.flipperOff();
//			Timer.delay(0.5);
//			Intake.intakeRev();
//			state = 90;
//		case 90:
//			driveForward(0.3);
//			//Intake.outGear();
//			Intake.intakeRev();
//			if(travelled(1)) {
//				state = 72;
//			}
		case 73:
			Intake.intakeRev();
//			Intake.flipperDown();
			for(int i = 0; i < 700; i++) { 
				if(i > 400) DriveBase.driver(0.3, 0.3);
				Timer.delay(1/100);
			}
			state = 80;
			DriveBase.resetEncoders();
			DriveBase.resetAHRS();
			break;
		case 80:
			Intake.intakeRev();
			Intake.flipperDown();
			driveForward(0.3);
			if(travelled(3)) {
				state = 100;
			}
		case 100:
			stayStill();
			Intake.flipperDown();
			//Intake.intakeOff();
//			Intake.flipperOff();
			Intake.intakeOff();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
}
	
	static void redMiddleTwoLeft(){
		Intake.updateFlipperPosition(); //To make all flipper setting functions work
		switch(state){
		case 10:
			driveForward(0.3);
			Intake.flipperBack();
//			Intake.flipperUp();
			waited(10, 0.1);
			if(travelled(74)) //76 inches
			{
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.placeGear();
			//Timer.delay(2);
			Intake.flipperUp();
			
			if(waited(70, 0.75)) {
				state = 71;
			}
			break;
		case 71:
//			driveForward(0.3);
			Intake.intakeRev();
			if(travelled(3.5)){
				state = 72; //SKIPPING
				DriveBase.resetEncoders();
			}
			break;
		case 72:
			Intake.intakeRev();
			if(waited(72, 0.5)) {
				state = 73;
			}
			break;
//		case 80:
//			stayStill();
//			Intake.intakeRev();
//			Intake.flipperOff();
//			Timer.delay(0.5);
//			Intake.intakeRev();
//			state = 90;
//		case 90:
//			driveForward(0.3);
//			//Intake.outGear();
//			Intake.intakeRev();
//			if(travelled(1)) {
//				state = 72;
//			}
		case 73:
			Intake.intakeRev();
			Intake.flipperDown();
			state = 74;
			DriveBase.resetEncoders();
			DriveBase.resetAHRS();
			break;
		case 74:
			driveBackward(0.4);
			if(travelled(-50)) {
				DriveBase.disablePID();
				state = 75;
			}
			break;
		case 75:
			turnLeft(0.3);
			if(turned(-85)) {
				DriveBase.resetEncoders();
				travelledDistance = 0;
				state = 80;
			}
			break;
		case 80:
	    	stayStill();
	    	DriveBase.resetAHRS();
	    	DriveBase.resetEncoders();
	    	DriveBase.disablePID();
	    	DriveBase.isVision = true;
	    	Robot.processGear = true;
	    	Robot.setCameraGear();
	    	Robot.useAngleFromCamera();
	    	if(waited(80, 0.5)) {
		    	state = 85;
	    	}
			break;
		case 85:
			Intake.flipperDown();
			Intake.intakeOn();
			driveForward(0.23);
			if(Intake.isLimit()) {
				stayStill();
				Intake.intakeOff();
				Intake.flipperBack();
				travelledDistance = (DriveBase.leftEncoder() + DriveBase.rightEncoder()) / 2;
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
				state = 90;
			}
			break;
		case 90:
			turnLeft(0.3);
			if(turned(-1)) {
				stayStill();
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
				state = 92;
			}
			break;
		case 92:
			driveBackward(0.3);
			if(travelled(-Math.abs(travelledDistance))) {
				state = 95;
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
			}
//			for(int i = 0; i < 720; i++) { 
//				DriveBase.driver(0.43, 0.4);
//				Timer.delay(1/100);
//			}
//			state = 95;
			break;
		case 95:
			stayStill();
			if(waited(95, 0.25)) {
				DriveBase.isVision = true;
				Robot.processGear = false;
				Robot.setCameraPeg();
				Robot.useAngleFromCamera();
				state = 100;
			}
			break;
		case 100:
			turnRight(0.3);
			if(turned(75)) {
				stayStill();
				state = 105;
			}
			break;
		case 102:
			driveForward(0.2);
			if(travelled(20)) {
				state = 105;
			}
			break;
		case 105:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(30) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 110;
				}
			}
			break;
		case 110:
			driveForward(0.21);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				Intake.flipperUp();
				state = 120;
			}
			break;
		case 120:
			stayStill();
			
			state = 130;
			break;
		case 130:
			stayStill();
//			Intake.placeGear();
//			Intake.intakeRev();
//			Timer.delay(1.5);
			//Intake.intakeRev();
			//driveForward(0.2);
			if(travelled(1)) {
				state = 150;	
			}
			break;
		case 140:
			DriveBase.isVision = false;
			Robot.useAngleFromNavx();
			//Intake.flipperDown();
			Timer.delay(1);
			//Intake.intakeRev();
			for(int i = 0; i < 600; i++) { 
				DriveBase.driver(0.15, 0.15);
				if(i > 300) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			
			state = 150;
			break;
		case 150:
			
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 160;
			}
			break;
		case 160:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 170;
			break;
//			Intake.outGear();
		case 170:
			stayStill();
			if (waited(101,1)){
				Intake.intakeRev();
				Timer.delay(2);
				Intake.intakeOff();
				state = 180;
			}
			break;
		case 180:
			stayStill();
			break;
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("travelled", travelledDistance);
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
}
	
	static void redRight(){
	switch(state){
	case 10:
		DriveBase.isVision = false;
		driveForward(0.3);
		Intake.intakeOff();
		Intake.flipperOff();
		Robot.useAngleFromNavx();
		if(travelled(68))
		{
			state = 30;
		}
		break;
	case 30:
		stayStill();
		//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
		if(waited(30, 1)) {
			state = 40;
		}
		break;
	case 40:
		turnLeft(0.18);
		if(turned(-46))//-46
		{
			DriveBase.resetAHRS();
			state = 50;
		}
		break;
	case 41:
		stayStill();
		waited(41, 0.1);
//		Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
		state = 41;
		break;
	case 50:
		stayStill();
		if(waited(50, 1)) {
			DriveBase.resetEncoders();
			DriveBase.resetAHRS();
			state = 60;
		}
		break;
	case 60:
		driveForward(0.2);
		//Intake.flipperUp();
		if(travelled(20) || Robot.visionTargetFound) {
			if(waited(60, 0.5)) {
				stayStill(); //RESET THE PID OBJECT
				DriveBase.isVision = true;
				Robot.useAngleFromCamera();
				state = 65;
			}
		}
		break;
	case 65:
		driveForward(0.195);
		//Intake.flipperUp();
		
		if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
			state = 66;
		}
		break;
	case 66:
		stayStill();
		
		state = 80;
		break;
	case 70:
		stayStill();
//		Intake.placeGear();
//		Intake.intakeRev();
//		Timer.delay(1.5);
		//Intake.intakeRev();
		//driveForward(0.2);
		if(travelled(1)) {
			state = 80;	
		}
		break;
	case 72:
		DriveBase.isVision = false;
		Robot.useAngleFromNavx();
		Intake.flipperDown();
		Timer.delay(1);
		Intake.intakeRev();
		for(int i = 0; i < 600; i++) { 
			DriveBase.driver(0.15, 0.15);
			if(i > 300) Intake.updateFlipperPosition();
			Timer.delay(1/100);
		}
		
		state = 100;
		break;
	case 80:
		
		Intake.flipperUp();
		driveForward(0.15);
		if(travelled(0.1)) {
			state = 100;
		}
		break;
	case 100:
		stayStill();
		//Intake.intakeOff();
		
		Intake.flipperOff();
		state = 101;
		break;
//		Intake.outGear();
	case 101:
		stayStill();
		if (waited(101,1)){
			Intake.intakeRev();
			Timer.delay(2);
			Intake.intakeOff();
			state = 102;
		}
		break;
	case 102:
		stayStill();
		break;
	default:
		//Um . . .
		break;
		}
	SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
	SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
	SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
	SmartDashboard.putNumber("current state", state);
	}
	
	static void redLeft(){
		switch(state){
		case 10:
			DriveBase.isVision = false;
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			Robot.useAngleFromNavx();
			if(travelled(68.5))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			if(waited(30, 1)) {
				state = 40;
			}
			break;
		case 40:
			turnRight(0.18);
			if(turned(49.2))//-46
			{
				DriveBase.resetAHRS();
				state = 50;
			}
			break;
		case 41:
			stayStill();
			waited(41, 0.1);
//			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 41;
			break;
		case 50:
			stayStill();
			if(waited(50, 1)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				state = 60;
			}
			break;
		case 60:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(20) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 65;
				}
			}
			break;
		case 65:
			driveForward(0.195);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				state = 66;
			}
			break;
		case 66:
			stayStill();
			
			state = 80;
			break;
		case 70:
			stayStill();
//			Intake.placeGear();
//			Intake.intakeRev();
//			Timer.delay(1.5);
			//Intake.intakeRev();
			//driveForward(0.2);
			if(travelled(1)) {
				state = 80;	
			}
			break;
		case 72:
			DriveBase.isVision = false;
			Robot.useAngleFromNavx();
			Intake.flipperDown();
			Timer.delay(1);
			Intake.intakeRev();
			for(int i = 0; i < 600; i++) { 
				DriveBase.driver(0.15, 0.15);
				if(i > 300) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			
			state = 100;
			break;
		case 80:
			
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 101;
			break;
//			Intake.outGear();
		case 101:
			stayStill();
			if (waited(101,1)){
				Intake.intakeRev();
				Timer.delay(2);
				Intake.intakeOff();
				state = 102;
			}
			break;
		case 102:
			stayStill();
			break;
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
		}

	static void redRightLong(){
		switch(state){
		case 10:
			DriveBase.isVision = false;
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			Robot.useAngleFromNavx();
			if(travelled(68))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			if(waited(30, 1)) {
				state = 40;
			}
			break;
		case 40:
			turnLeft(0.2);
			if(turned(-46))//-46
			{
				DriveBase.resetAHRS();
				state = 41;
			}
			break;
		case 41:
			stayStill();
			waited(41, 0.1);
//			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 50;
			break;
		case 50:
			stayStill();
			if(waited(50, 0.25)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				
				state = 60;
			}
			break;
		case 60:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(20) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 65;
				}
			}
			break;
		case 65:
			driveForward(0.195);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				state = 66;
			}
			break;
		case 66:
			stayStill();
			
			state = 68;
			break;
		case 68:
			DriveBase.isVision= false;
			Robot.useAngleFromNavx();
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 71;
			break;
//			Intake.outGear();
		case 71:
			stayStill();
			if (waited(101, 0.5)){
				Intake.intakeRev();
				Timer.delay(0.5);
				Intake.intakeOff();
				state = 74;
			}
			break;
		case 74:
			for(int i = 0; i < 600; i++) { 
				if(i > 20) DriveBase.driver(0.3, 0.3);
				if(i > 75) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			state = 75;
			Robot.useAngleFromNavx();
			DriveBase.isVision = false;
			stayStill();
			//state = 100;
			break;
		case 75:
			stayStill();
			if(waited(75, 0.25)) {
				state = 76;
			}
			break;
		case 76:
			turnRight(0.3);
			if(turned(22)) {
				state = 77;
			}
			break;
		case 77:
			stayStill();
			if(waited(77, 0.25)) {
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
//				DriveBase.disablePID();
				DriveBase.isVision = false;
				state = 78;
				
			}
			break;
		case 78:
			Intake.flipperBack();
			driveForward(0.8);
			if(travelled(230)) {
				stayStill();
				state = 79;
			}
			break;
		case 79:
			Intake.flipperBack();
			turnLeft(0.21);
			if(turned(-15)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				stayStill();
				state = 80;
			}
			break;
		case 80:
			Intake.flipperBack();
			driveForward(0.8);
			if(travelled(100)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.intakeOff();
//			Intake.outGear();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
		}

	static void blueLeftLong(){
		switch(state){
		case 10:
			DriveBase.isVision = false;
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			Robot.useAngleFromNavx();
			if(travelled(68))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			if(waited(30, 1)) {
				state = 40;
			}
			break;
		case 40:
			turnRight(0.2);
			if(turned(43))//-46
			{
				DriveBase.resetAHRS();
				state = 41;
			}
			break;
		case 41:
			stayStill();
			waited(41, 0.1);
//			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 50;
			break;
		case 50:
			stayStill();
			if(waited(50, 0.25)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				
				state = 60;
			}
			break;
		case 60:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(20) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 65;
				}
			}
			break;
		case 65:
			driveForward(0.195);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				state = 66;
			}
			break;
		case 66:
			stayStill();
			
			state = 68;
			break;
		case 68:
			DriveBase.isVision= false;
			Robot.useAngleFromNavx();
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 71;
			break;
//			Intake.outGear();
		case 71:
			stayStill();
			if (waited(101, 0.5)){
				Intake.intakeRev();
				Timer.delay(0.5);
				Intake.intakeOff();
				state = 74;
			}
			break;
		case 74:			
			for(int i = 0; i < 600; i++) { 
				if(i > 20) DriveBase.driver(0.3, 0.3);
				if(i > 75) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			state = 75;
			Robot.useAngleFromNavx();
			DriveBase.isVision = false;
			stayStill();
			//state = 100;
			break;
		case 75:
			stayStill();
			if(waited(75, 0.25)) {
				state = 76;
			}
			break;
		case 76:
			turnLeft(0.3);
			if(turned(-22)) {
				state = 77;
			}
			break;
		case 77:
			stayStill();
			if(waited(77, 0.25)) {
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
//				DriveBase.disablePID();
				DriveBase.isVision = false;
				state = 78;
				
			}
			break;
		case 78:
			Intake.flipperBack();
			driveForward(0.7);
			if(travelled(220)) {
				stayStill();
				state = 79;
			}
			break;
		case 79:
			Intake.flipperBack();
			turnRight(0.21);
			if(turned(30)) {
				stayStill();
				state = 80;
			}
			break;
		case 80:
			stayStill();
			if(waited(80, 0.5)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				state = 81;
			}
			break;
		case 81:
			Intake.flipperBack();
			driveForward(0.8);
			if(travelled(150)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.intakeOff();
//			Intake.outGear();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
		}

	
	static void blueRightLong(){
		switch(state){
		case 10:
			DriveBase.isVision = false;
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			Robot.useAngleFromNavx();
			if(travelled(68))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			if(waited(30, 1)) {
				state = 40;
			}
			break;
		case 40:
			turnLeft(0.2);
			if(turned(-46))//-46
			{
				DriveBase.resetAHRS();
				state = 41;
			}
			break;
		case 41:
			stayStill();
			waited(41, 0.1);
//			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 50;
			break;
		case 50:
			stayStill();
			if(waited(50, 0.25)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				
				state = 60;
			}
			break;
		case 60:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(20) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 65;
				}
			}
			break;
		case 65:
			driveForward(0.195);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				state = 66;
			}
			break;
		case 66:
			stayStill();
			
			state = 68;
			break;
		case 68:
			DriveBase.isVision= false;
			Robot.useAngleFromNavx();
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 71;
			break;
//			Intake.outGear();
		case 71:
			stayStill();
			if (waited(101, 0.5)){
				Intake.intakeRev();
				Timer.delay(0.5);
				Intake.intakeOff();
				state = 74;
			}
			break;
		case 74:
			for(int i = 0; i < 600; i++) { 
				if(i > 20) DriveBase.driver(0.3, 0.3);
				if(i > 75) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			state = 75;
			Robot.useAngleFromNavx();
			DriveBase.isVision = false;
			stayStill();
			//state = 100;
			break;
		case 75:
			stayStill();
			if(waited(75, 0.25)) {
				state = 76;
			}
			break;
		case 76:
			turnRight(0.3);
			if(turned(22)) {
				state = 77;
			}
			break;
		case 77:
			stayStill();
			if(waited(77, 0.25)) {
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
//				DriveBase.disablePID();
				DriveBase.isVision = false;
				state = 78;
				
			}
			break;
		case 78:
			Intake.flipperBack();
			driveForward(0.8);
			if(travelled(230)) {
				stayStill();
				state = 80;
			}
			break;
		case 80:
			Intake.flipperBack();
			driveForward(0.5);
			if(travelled(40)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.intakeOff();
//			Intake.outGear();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
		}
	
	static void redLeftLong(){
		switch(state){
		case 10:
			DriveBase.isVision = false;
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			Robot.useAngleFromNavx();
			if(travelled(68.5))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			//Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			if(waited(30, 1)) {
				state = 40;
			}
			break;
		case 40:
			turnRight(0.18);
			if(turned(49.2))//-46
			{
				DriveBase.resetAHRS();
				state = 50;
			}
			break;
		case 41:
			stayStill();
			waited(41, 0.1);
//			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 41;
			break;
		case 50:
			stayStill();
			if(waited(50, 0.25)) {
				DriveBase.resetEncoders();
				DriveBase.resetAHRS();
				
				state = 60;
			}
			break;
		case 60:
			driveForward(0.2);
			//Intake.flipperUp();
			if(travelled(20) || Robot.visionTargetFound) {
				if(waited(60, 0.5)) {
					stayStill(); //RESET THE PID OBJECT
					DriveBase.isVision = true;
					Robot.useAngleFromCamera();
					state = 65;
				}
			}
			break;
		case 65:
			driveForward(0.195);
			//Intake.flipperUp();
			
			if(Robot.isOnTarget()) { //travelled(26)) { //44 total inches	//DONT RUN INTO THE THING
				state = 66;
			}
			break;
		case 66:
			stayStill();
			
			state = 68;
			break;
		case 68:
			DriveBase.isVision= false;
			Robot.useAngleFromNavx();
			Intake.flipperUp();
			driveForward(0.15);
			if(travelled(0.1)) {
				state = 70;
			}
			break;
		case 70:
			stayStill();
			//Intake.intakeOff();
			
			Intake.flipperOff();
			state = 71;
			break;
//			Intake.outGear();
		case 71:
			stayStill();
			if (waited(101, 0.5)){
				Intake.intakeRev();
				Timer.delay(0.5);
				Intake.intakeOff();
				state = 74;
			}
			break;
		case 74:
			
			for(int i = 0; i < 600; i++) { 
				if(i > 20) DriveBase.driver(0.3, 0.3);
				if(i > 75) Intake.updateFlipperPosition();
				Timer.delay(1/100);
			}
			state = 75;
			Robot.useAngleFromNavx();
			DriveBase.isVision = false;
			stayStill();
			//state = 100;
			break;
		case 75:
			stayStill();
			if(waited(75, 0.25)) {
				state = 76;
			}
			break;
		case 76:
			turnLeft(0.3);
			if(turned(-22)) {
				state = 77;
			}
			break;
		case 77:
			stayStill();
			if(waited(77, 0.25)) {
				DriveBase.resetAHRS();
				DriveBase.resetEncoders();
//				DriveBase.disablePID();
				DriveBase.isVision = false;
				state = 78;
				
			}
			break;
		case 78:
			Intake.flipperBack();
			driveForward(0.8);
			if(travelled(240)) {
				stayStill();
				state = 80;
			}
			break;
		case 80:
			Intake.flipperBack();
			driveForward(0.5);
			if(travelled(40)) {
				state = 100;
			}
			break;
		case 100:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.intakeOff();
//			Intake.outGear();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
		}
	
	static void testPID(){
		double distanceDiff = DriveBase.leftEncoder() - DriveBase.rightEncoder();
		
		double diffRatio = 0.3;
		
		double newPower = (distanceDiff * diffRatio) / 3;
		
		double wantedPower = -0.3;
		
		
		if(newPower > 0) {
			DriveBase.driver(wantedPower + newPower, wantedPower - newPower);
		} else {
			DriveBase.driver(wantedPower - newPower, wantedPower + newPower);
		}
		
		SmartDashboard.putNumber("distance Diff", distanceDiff);
	}*/
	
}