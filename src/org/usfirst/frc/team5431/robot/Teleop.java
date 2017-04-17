package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.utils.TitanJoystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleop {
	private static TitanJoystick driverJoy, operatorJoy; 
	private static boolean initialized = false;
	private static int intakePrev = 0; //Toggle state for intake
	
	public static void init() {
		if(initialized) return;
		
		//Initialized the joysticks (Look at Constants for IDs)
		driverJoy = new TitanJoystick(Constants.Joystick.driverId);
		operatorJoy = new TitanJoystick(Constants.Joystick.operatorId);
		
		//Set the default deadZones for both joysticks
		driverJoy.setDeadZone(Constants.Joystick.deadZone);
		operatorJoy.setDeadZone(Constants.Joystick.deadZone);
		
		initialized = true;
	}
	
	public static void periodicDrive() {
		DriveBase.driver(driverJoy.getLeftY(), driverJoy.getRightY());
	}
	
	public static void periodicFlipper() {
    	if(operatorJoy.getXButton()){
    		Intake.flipperUp();
    	} else if(operatorJoy.getLeftBumper()){
    		Intake.flipperDown();
    	} else if(operatorJoy.getRightBumper()){
    		Intake.flipperBack();
    	}
    	
    	//Must be called to change the flipper position
    	Intake.updateFlipperPosition();
	}
	
	public static void periodicIntake() {
		boolean Ybutton = operatorJoy.getYButton();
		int intakeTrigger = (operatorJoy.getLeftTrigger() ? 0 : 1);
		
    	if(intakePrev > intakeTrigger){
    		Intake.toggleIntake();
    	} else if(Ybutton) {
    		Intake.intakeOn();
    	} else if((Intake.isIntakeOn() && Intake.isLimit() && !Ybutton) || operatorJoy.getBButton()) { //when limit IS pushed
    		Intake.intakeOff();
        	Intake.flipperBack();
    	} else if(operatorJoy.getRightTrigger()){
			Intake.intakeRev();
		} else {
			if(!Intake.isIntakeOn()){
				Intake.intakeOff();
			}
		}
    	
    	intakePrev = intakeTrigger;
	}
	
	public static void periodicClimber() {
    	if(operatorJoy.getBackButton()) {
			Intake.climbSlow();
		} else if(operatorJoy.getAButton()){
			Intake.climb();
		} else{
			Intake.climbOff();
		}
	}
	
	public static void periodicDebug() {
    	SmartDashboard.putBoolean("gearIn", Intake.isLimit()); 
    	SmartDashboard.putBoolean("IsIntaking", Intake.isIntakeOn());
		SmartDashboard.putNumber("halEncoder", Intake.getPosition());
		SmartDashboard.putNumber("rigt encoder value teleop", DriveBase.rightEncoder());
		SmartDashboard.putNumber("left encoder value teleop", DriveBase.leftEncoder());
		SmartDashboard.putNumber("yaw teleop", DriveBase.getYaw());
	}
}
