
package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import org.usfirst.frc.team5431.robot.*;

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
	int flipperToggle = 0;
	boolean isFlipperDown = true;
	int prev = 0;
	NetworkTable table;
	
	LED led;
	
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
    	SmartDashboard.putNumber("go to hel", 2);
    	SmartDashboard.putBoolean("gearIn", false);
    	table = NetworkTable.getTable("copernicus");
    	
    	new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(360,240);
            camera.setFPS(30);
        }).start();
    	
    	LED.init();
    	

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
     	LED.setTimeElapsed(Timer.getMatchTime());

    	LED.setGear(Intake.isLimit());
    }
    
    public void autonomousInit() {
    	Auton.state = 10;
    	
    	DriveBase.resetEncoders();
    	DriveBase.resetAHRS();
    	//drivePid.driveController.disable();
    	//drivePid.driveController.enable();
    	DriveBase.masterRight.setVoltageRampRate(0);
    	DriveBase.masterLeft.setVoltageRampRate(0);
    	//autoLeftDriveController.enable();
    	//autoLeftDriveController.reset();
    	//autoLeftDriveController.setSetpoint(0.5);
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	SmartDashboard.putNumber("auton??", 1);
    	Auton.run(1);
    	//run((int)table.getNumber("autonSelect", 0.0)); //1 is drive forward
    	//Auton.redMiddle();
    
    } 

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
    	DriveBase.resetEncoders();
    	DriveBase.resetAHRS();
    	//DriveBase.driveBaseInit();
    	//drivePid.driveController.disable();
    }
    
    public void teleopPeriodic() {
    	//table.putBoolean("gearIn", Intake.isLimit());
    	//table.putBoolean("intake", Intake.isIntakeOn());
    	SmartDashboard.putBoolean("gearIn", Intake.isLimit());
    	SmartDashboard.putBoolean("IsIntaking", Intake.isIntakeOn());
      	//Intake.intakeOn();
    	//Intake.setFlipperPosition(1);
    	DriveBase.driver(xBoxDrive.getRawAxis(1), xBoxDrive.getRawAxis(5));
    	if(xBoxOperate.getRawButton(6)){
    		Intake.flipperUp();
    	}
    	else if(xBoxOperate.getRawButton(5)){
    		Intake.flipperDown();
    	}else{
    		Intake.flipperOff();
    		
    	}
    	    	
    	if(prev > (xBoxOperate.getRawAxis(2) > 0.5 ? 0:1)){
    		Intake.toggleIntake();
    	}
    	else if(Intake.isIntakeOn() && Intake.isLimit()){ //when limit IS pushed
    		Intake.intakeOff();
    	}
    	
    	else if(!Intake.isIntakeOn()){
    		if(xBoxOperate.getRawAxis(3) > 0.5){
    			Intake.intakeRev();    	
    		}
    		else if(xBoxOperate.getRawButton(1)){
    			Intake.climb();
    		}
    		else{
    			Intake.intakeOff();
    		}
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
