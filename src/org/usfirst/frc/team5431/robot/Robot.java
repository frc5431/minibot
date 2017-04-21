
package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.perception.Vision;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Robot extends IterativeRobot {
	
    public void robotInit() {
    	DriveBase.init();
    	Auton.init();
    	Teleop.init();
    	Intake.intakeInit();
    	NetworkTable.initialize();
    	
    	Vision.camera = CameraServer.getInstance().startAutomaticCapture();
    	
    	Vision.init();
    	//LED.init();
    	Test.init();
    }
 
    public void autonomousInit() {
    	//Set the autonomous starting state
    	Auton.autoInit();
    	
    	//Start the driveBase from scratch
    	DriveBase.reset();
    	DriveBase.disablePID();
    	DriveBase.setPIDNormal();
    	
    	//Bring the intake into the back position
    	Intake.rebaseIntake();
    	
    	//Vision.setCameraGear();
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	Auton.periodic();
    } 

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() { 
    	//Set the camera settings for regular driver view
    	Vision.setCameraNormal();
    	
    	DriveBase.reset();
    	DriveBase.disablePID();
    	
    	//Stop the flipper position change
    	Intake.flipperOff();
    }
    
    public void teleopPeriodic() {
    	Teleop.periodicDrive();
    	Teleop.periodicFlipper();
    	Teleop.periodicIntake();
    	Teleop.periodicClimber();
    	Teleop.periodicDebug();
    }
    
    public void disabledPeriodic() {
    	DriveBase.disablePID();
    	Vision.setGreenLedsOff();
    	Vision.setWhiteLedsOff();
    }
   
    public void robotPeriodic() {
    	Vision.periodic();
    	//LED.periodic();
    }
    
    public void testInit() {
    	Test.testInit();
    }
    
    public void testPeriodic() {
    	Test.periodic();
    }
    
}
