
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
	static Joystick joy;
	static Auton auton;
	static DriveBase drive;
	static Intake intake;
	int flipperToggle = 0;
	boolean flipperDown = true;
	
    public void robotInit() {
    	Auton auton = new Auton();
    	//Auton.init();
    	//enc1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	//enc2 = new Encoder(2, 3, false, Encoder.EncodingType.k4X); 
    	drive = new DriveBase();
    	//intake = new Intake();
    	joy = new Joystick(0);
    	intake = new Intake();
    	

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
    	drive.drive(joy.getRawAxis(1), joy.getRawAxis(5));
    	if(flipperToggle > joy.getRawButton(6))
    	{
    		if(!flipperDown)
    		{
    			intake.flipperDown();
    			flipperDown = true;
    		}
    		else
    		{
    			intake.flipperUp();
    			flipperDown = false;
    		}
    	}
    	
    	flipperToggle = joy.getRawButton(6);
    	
    	if(flipperDown)
    	{
    		if(intake.limitOn())
    			intake.intakeOn(1.0);
    		else
    			intake.intakeOff();
    	}
    	
    	if(joy.getRawButton(5))
    	{
    		//Put on gear
    	}
    	
    	if(joy.getRawAxis(3) > 0.5)
    	{
    		//Climb
    	}
    }
   
    public void testPeriodic() {
    
    }
    
}
