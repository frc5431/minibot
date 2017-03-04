
package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team5431.robot.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Joystick joy;
	DriveBase drive;
	Intake intake;
	Encoder enc1;
	Encoder enc2;
	CANTalon flipper;
	CANTalon intakeMotor;

	
    public void robotInit() {
    	Auton auton = new Auton(1);
    	auton.init();
    	//Auton.init();
    	//enc1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	//enc2 = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
    	drive = new DriveBase();
    	//intake = new Intake();
    	joy = new Joystick(0);
    	flipper = new CANTalon(constants.Flipper);
    	intakeMotor = new CANTalon(constants.Intake);

    }
    
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	drive.setLeftRight(joy.getRawAxis(1), joy.getRawAxis(5));
    	if(joy.getRawAxis(3)>0.5){
    		intakeMotor.set(-joy.getRawAxis(3));
    	}
    	else if(joy.getRawAxis(2)>0.5){
    		intakeMotor.set(joy.getRawAxis(2));
    	}
    	else{
    		intakeMotor.set(0);
    	}
    	
    	if(joy.getRawButton(3)){
    		flipper.set(-0.5);
    	}
    	else if(joy.getRawButton(2)){
    		flipper.set(0.5);
    	}
    	else{
    		flipper.set(0);
    	}
    	//SmartDashboard.putNumber("Value", intake.setFlipperPosition());
    	//SmartDashboard.putNumber("encoder1", enc1.get());
    	//SmartDashboard.putNumber("encoder2", enc2.get());
    	}
   
    public void testPeriodic() {
    
    }
    
}
