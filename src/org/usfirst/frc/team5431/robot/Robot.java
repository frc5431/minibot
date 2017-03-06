
package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
	Joystick xbox;
	Joystick extreme;
	//DriveBase drive;
	int flipperToggle = 0;
	boolean isFlipperDown = true;
	
    public void robotInit() {
    	//Auton auton = new Auton();
    	//Auton.init();
    	//enc1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	//enc2 = new Encoder(2, 3, false, Encoder.EncodingType.k4X); 
    	//drive = new DriveBase();
    	//Intake = new Intake();
    	xbox = new Joystick(0);
    	extreme = new Joystick(1);
    	CameraServer.getInstance().startAutomaticCapture();
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
    public void teleopInit(){
    	DriveBase.driveBaseInit();
    	Intake.intakeInit();
    }
    
    public void teleopPeriodic() {
    	//Intake.intakeOn();
    	//Intake.setFlipperPosition(1);
    	DriveBase.driver(xbox.getRawAxis(1), xbox.getRawAxis(5));
    	if(extreme.getRawButton(7)){
    		Intake.flipperUp();
    	}
    	else if(extreme.getRawButton(8)){
    		Intake.flipperDown();
    	}else{
    		Intake.flipperOff();
    		
    	}
    	
    	if(extreme.getRawButton(9) && Intake.isLimit()){
    		Intake.intakeOn(); 
    	}
    	else if(extreme.getRawButton(10)){
    		Intake.intakeRev();    	
    	}
    	else if(extreme.getRawButton(11)){
    		Intake.climb();
    	}
    	else{
    		Intake.intakeOff();
    	}
    	
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
