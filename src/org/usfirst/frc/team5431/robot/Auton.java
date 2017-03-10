package org.usfirst.frc.team5431.robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


class Auton{
	static int state = 10;

	static boolean travelled(double distance){
		if (DriveBase.leftEncoder() >= distance || DriveBase.rightEncoder() >= distance){
			SmartDashboard.putNumber("reached encoder distance", 1);
			DriveBase.resetEncoders();
			return true;
		}
		else{
			SmartDashboard.putNumber("reached encoder distance", 0);
			return false;
		}
	}
	
	static boolean turned(double degrees){
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
	}
	
	static void driveForward(double Power){		
		double distanceDiff = DriveBase.getYaw() / 4;//DriveBase.leftEncoder() - DriveBase.rightEncoder();
		
		double diffRatio = 0.1;
		
		double newPower = (distanceDiff * diffRatio) / 3;
		
		double wantedPower = -0.3;
		
		
		if(newPower > 0) {
			DriveBase.driver(wantedPower, wantedPower - newPower);
		} else {
			DriveBase.driver(wantedPower + newPower, wantedPower);
		}
		//DriveBase.driver(-power,-power);
		//DriveBase.driver(-power, -power - 0.115);
	}
	
	static void driveBackward(double power){
		DriveBase.driver(power, power);
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
	
	static void run(int selection)
	{
		switch(selection)
		{
		case 0:
			stayStill();
			break;
		case 1: 
			DriveForward();
			break;
		case 2:
			RedLeft();
			break;
		case 3:
			redMiddle();
			break;
		case 4:
			redRight();
			break;
		case 5:
			testPID();
		default:
			break;
		}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
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
	
	static void RedLeft(){
		switch(state)
		{
		case 10:
			driveForward(0.3);
			Intake.intakeOff();
			Intake.flipperOff();
			if(travelled(77))
			{
				state = 30;
			}
			break;
		case 30:
			stayStill();
			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 40;
			break;
		case 40:
			turnRight(0.3);
			if(turned(49))
			{
				state = 50;
			}
			break;
		case 50:
			stayStill();
			Timer.delay(2);//BAD PRACTICE
			DriveBase.resetEncoders();
			state = 60;
			break;
			
		case 60:
			driveForward(0.27);
			if(travelled(30))//32
			{
				state = 70;
			}
			break;
		case 70:
			Intake.placeGear();
			driveForward(0.3);
			if(travelled(3)){
				state = 80;
			}
			break;
		case 80:
			stayStill();
			Intake.intakeOff();
			Intake.flipperOff();
		default:
			//Um . . .
			break;
		}
	}
	
	static void redMiddle(){
		switch(state){
		case 10:
			driveForward(0.25);
			Intake.intakeOff();
			Intake.flipperOff();
			if(travelled(68))
			{
				state = 70;
			}
			break;
		case 70:
			stayStill();
			Intake.placeGear();
			Timer.delay(2);
			state = 71;
			break;
		case 71:
			driveForward(0.3);
			if(travelled(5)){
				state = 80;
				DriveBase.resetEncoders();
			}
			break;
		case 80:
			stayStill();
			Intake.intakeOff();
			Intake.flipperOff();
			Timer.delay(0.3);
			Intake.outGear();
			state = 90;
		case 90:
			driveForward(0.3);
			Intake.outGear();
			if(travelled(2)) {
				state = 100;
			}
		case 100:
			stayStill();
			//Intake.intakeOff();
			Intake.flipperOff();
			Intake.outGear();
		default:
			//Um . . .
			break;
			}
		SmartDashboard.putNumber("rigt encoder value auton", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value auton", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw auton", DriveBase.getYaw());
		SmartDashboard.putNumber("current state", state);
}
	
	static void redRight(){
	switch(state){
	case 10:
		driveForward(0.3);
		Intake.intakeOff();
		Intake.flipperOff();
		if(travelled(56))
		{
			state = 30;
		}
		break;
	case 30:
		stayStill();
		Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
		state = 40;
		break;
	case 40:
		turnLeft(0.15);
		if(turned(-38))
		{
			DriveBase.resetAHRS();
			state = 50;
		}
		break;
	case 50:
		stayStill();
		Timer.delay(2);//BAD PRACTICE
		DriveBase.resetEncoders();
		DriveBase.resetAHRS();
		state = 60;
		break;
		
	case 60:
		driveForward(0.3);
		if(travelled(52))
		{
			state = 70;
		}
		break;
	case 70:
		stayStill();
		Intake.placeGear();
		Timer.delay(1.5);
		state = 71;
		break;
	case 71:
		driveForward(0.3);
		if(travelled(6)){
			state = 75;
		}
		break;
	case 80:
		stayStill();
		Intake.intakeOff();
		Intake.flipperOff();
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
	}
	
}