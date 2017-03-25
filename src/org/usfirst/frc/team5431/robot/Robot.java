
package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import org.usfirst.frc.team5431.robot.*;
import org.usfirst.frc.team5431.utils.TitanDrive;
import org.usfirst.frc.team5431.utils.TitanNavx;
import org.usfirst.frc.team5431.utils.TitanTalon;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	Joystick xBoxDrive;
	Joystick xBoxOperate;
	//DrivePID drivePid;
	//DriveBase drive;
	CANTalon masterLeft, masterRight,bRight,bLeft;
	int flipperToggle = 0;
	boolean isFlipperDown = true;
	int prev = 0;
	NetworkTable table;
	//SendableChooser<Integer> autoChooser;
	boolean isPullingBack = false;
	int autoSelected = 1;
	
/*	TitanTalon frontLeft, frontRight, rearLeft, rearRight;
	TitanDrive drive;
	TitanNavx navx;
	RobotDrive newDrive;*/
		
    public void robotInit() {
    	//Auton auton = new Auton();
    	//Auton.init();
    	//enc1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	//enc2 = new Encoder(2, 3, false, Encoder.EncodingType.k4X); 
    	//drive = new DriveBase();
    	//Intake = new Intake();
    	//drivePid = new DrivePID();
    	
    	xBoxDrive = new Joystick(0);
    	xBoxOperate = new Joystick(1);
    	NetworkTable.initialize();
    	SmartDashboard.putNumber("DIFRAT",0.0);
    	SmartDashboard.putNumber("go to hel", 2);
    	SmartDashboard.putBoolean("gearIn", false);
    	SmartDashboard.putNumber("AutonomousSelection", 3);
    	table = NetworkTable.getTable("copernicus");
    	
    	new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(360,240);
            camera.setFPS(30); 
        }).start();
    	
    	LED.init();
    
    	//navx = new TitanNavx();
    	//navx.zeroYaw();
    	
    	/*frontLeft = new TitanTalon(constants.masterLeftId);
    	frontRight = new TitanTalon(constants.masterRightId);
    	rearLeft = new TitanTalon(constants.bLeftId);
    	rearRight = new TitanTalon(constants.bRightId);
    	
    	drive = new TitanDrive(frontLeft, frontRight, rearLeft, rearRight);
    	drive.setTurnBand(0.2);
    	drive.setDriveBand(0.1);
    	drive.setBrakeMode(false);
    	drive.setSlopeHandler(0.85, 0.15);
    	drive.setResetCountLimit(300);
    	int reset[] = new int[] {0};
    	
    	drive.setDriveController(new TitanDrive.DriveController() {
			@Override
			public double getGlobalAngle() {
				double navxAngle = navx.getYaw();
				SmartDashboard.putNumber("NAVX_ANGLE", navxAngle);
				return navxAngle; //get navx angle
			}

			@Override
			public void resetGlobalAngle() {
				SmartDashboard.putNumber("RESETNAVX", reset[0]++);
				navx.zeroYaw();
				navx.resetDisplacement();
				navx.resetYaw();
			}
		});*/
    	
    	
    	/*masterLeft = new CANTalon(constants.masterLeftId);
    	masterRight = new CANTalon(constants.masterRightId);
    	
    	bLeft = new CANTalon(constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(constants.masterLeftId);
  
    	
    	
    	bRight = new CANTalon(constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(constants.masterRightId);
    	
    	newDrive = new RobotDrive(masterLeft,masterRight);*/
    	
    	
    	//autoChooser = new SendableChooser<Integer>();
    	//autoChooser.addObject("StayStill", 0);
    	//autoChooser.addDefault("BaseLine", 1);
    	//autoChooser.addObject("TurnRight(Gear)", 2);
    	//autoChooser.addObject("Middle(Gear)", 3);
    	//autoChooser.addObject("TurnLeft(Gear)", 4);
    	

    	//driveLeftPIDInput = new DrivePID.DriveInputSource(1);
    	//driveRightPIDInput = new DrivePID.DriveInputSource(2);
    	/*drivePIDOutput = new DrivePID.DriveOutputSource();
    	
    	autoLeftDriveController = new PIDController(1, 0, 0, 0, drivePIDInput, drivePIDOutput);
    	autoLeftDriveController.setInputRange(-1000, 1000);
    	autoLeftDriveController.setAbsoluteTolerance(0.5);
    	autoLeftDriveController.setOutputRange(-0.5, 0.5);
    	autoLeftDriveController.setToleranceBuffer(40);*/
    	
    	
    	
    	DriveBase.driveBaseInit();
    	Intake.intakeInit();

    }
    
    public void robotPeriodic(){
     	LED.setTimeElapsed(150 - Timer.getMatchTime());

    	LED.setGear(Intake.isLimit());
    }
    
    public void autonomousInit() {
    	Auton.state = 10;
    	autoSelected = (int) SmartDashboard.getNumber("AutonomousSelection", 3);//autoChooser.getSelected();
    	
    	DriveBase.resetEncoders();
    	DriveBase.resetAHRS();
    	//drivePid.driveController.disable();
    	//drivePid.driveController.enable();
    	DriveBase.masterRight.setVoltageRampRate(0);
    	DriveBase.masterLeft.setVoltageRampRate(0);
    	//autoLeftDriveController.enable();
    	//autoLeftDriveController.reset();
    	//autoLeftDriveController.setSetpoint(0.5);
    	
    	DriveBase.driver(-0.3, -0.3);
    	DriveBase.pidOutput.wantedPower = -0.3;
    	DriveBase.enablePID();
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	/*SmartDashboard.putNumber("auton??", 1);
    	Auton.run(autoSelected);*/
    	//Auton.DriveForward();
    	//Auton.redMiddle();
    	//Auton.blueLeft();
    	//Auton.redRight();
    	//run((int)table.getNumber("autonSelect", 0.0)); //1 is drive forward
    	//Auton.redMiddle();
    
    } 

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
    	DriveBase.resetEncoders();
    	//drive.resetGlobalAngle();
    	DriveBase.resetAHRS();
    	DriveBase.disablePID();
    	for(int a = 0; a < 5000; a++) {
    		if(!Intake.flipperLimit.get()) break;
    		Intake.flipper.set(1);
    		Timer.delay(0.05);
    	}
    	Intake.position = 0;
    	Intake.halCount.reset();
    	//DriveBase.driveBaseInit();
    	//drivePid.driveController.disable();
    }
    
    public void teleopPeriodic() {
    	//drive.titanDrive(-xBoxDrive.getRawAxis(1), xBoxDrive.getRawAxis(4));
    	//newDrive.arcadeDrive(xBoxDrive.getRawAxis(1), -xBoxDrive.getRawAxis(4), true);
    	//table.putBoolean("gearIn", Intake.isLimit());
    	//table.putBoolean("intake", Intake.isIntakeOn());
    	SmartDashboard.putBoolean("gearIn", Intake.isLimit()); 
    	SmartDashboard.putBoolean("IsIntaking", Intake.isIntakeOn());
    	
      	//Intake.intakeOn();
    	
    	/*if(isPullingBack) {
    		if(!Intake.flipperLimit.get()) isPullingBack = false;
    		Intake.flipperBack();
    	}*/
    	
    	//Intake.setFlipperPosition(1);
    	Intake.updateFlipperPosition();
    	SmartDashboard.putNumber("halEncoder", Intake.getPosition());
    	DriveBase.driver(xBoxDrive.getRawAxis(1), xBoxDrive.getRawAxis(5));
    	if(xBoxOperate.getRawButton(3)){
    		Intake.flipperUp();
    	}
    	else if(xBoxOperate.getRawButton(5)){
    		Intake.flipperDown();
    	}
    	else if(xBoxOperate.getRawButton(6)){
    		Intake.flipperBack();
    	}
    	else{
    		//Intake.flipperOff();
    	}
    	
    	if(prev > (xBoxOperate.getRawAxis(2) > 0.5 ? 0:1)){
    		Intake.toggleIntake();
    	}
    	else if(xBoxOperate.getRawButton(4)) {
    		Intake.intakeOn();
    	}
    	else if((Intake.isIntakeOn() && Intake.isLimit() && !xBoxOperate.getRawButton(4)) || xBoxOperate.getRawButton(2)){ //when limit IS pushed
    		Intake.intakeOff();
    		//isPullingBack = true;
        	Intake.flipperBack();
    		//Intake.flipper.set(0.5);
    	}else if(xBoxOperate.getRawAxis(3) > 0.5){
			Intake.intakeRev();    	
		}else{
			if(!Intake.isIntakeOn()){
				Intake.intakeOff();
			}
		}
    	/*else if(prev < (xBoxOperate.getRawAxis(3) > 0.5 ? 0:1)) {
    		Intake.intakeOff();
    	}*/
    	
    	if(xBoxOperate.getRawButton(7)) {
			Intake.climbSlow();
		}
		else if(xBoxOperate.getRawButton(1)){
			Intake.climb();
		} else{
			//Intake.intakeOff();
			Intake.climbOff();
		}
    	
		
    	
    	prev = xBoxOperate.getRawAxis(2) > 0.5 ? 0:1;
    	
		SmartDashboard.putNumber("rigt encoder value teleop", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value teleop", DriveBase.leftEncoder());
    	
		SmartDashboard.putNumber("yaw teleop", DriveBase.getYaw());
		
		/*if (!Intake.isLimit()){
			
		}
		else{
			SmartDashboard.putBoolean("gearIn", false);
		}*/
    	
    }
    	
    	/*if(xbox.getRawAxis(2)>0.5){
    		Intake.intakeOn();
    	}
    }    	
    
    
    
    	if(flipperToggle > (xbox.getRawButton(6) ? 0:1))
    	{
    		if(!isFlipperDown)
    		{
    			Intake.flipperDown();
    			isFlipperDown = true;
    		}
    		else
    		{
    			Intake.flipperUp();
    			isFlipperDown = false;
    		}
    	}
    	
    	flipperToggle = xbox.getRawButton(6) ? 0:1;
    	
    	if(isFlipperDown)
    	{
    		if(Intake.limitOn())
    			Intake.intakeOn(1.0);
    		else
    			Intake.intakeOff();
    	}
    	
    	if(xbox.getRawButton(5))
    	{
    		//Put on gear
    	}
    	
    	if(xbox.getRawAxis(3) > 0.5)
    	{
    		Intake.climb();
    	}
    }*/
   
    public void testPeriodic() {
    
    }
    
}
