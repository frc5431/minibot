package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.perception.Vision;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Test {
	//Test selection
	public static SendableChooser<Integer> testChooser;
	public static boolean initialized = false;
	public static int testSelected = 10;
	
	public static void init() {
		if(initialized) return;
		testChooser = new SendableChooser<Integer>();
		testChooser.addDefault("FlipperUp", 10);
		testChooser.addObject("FlipperBack", 15);
		testChooser.addObject("FlipperDown", 20);
		testChooser.addObject("DriveForward", 30);
		testChooser.addObject("DriveBackward", 40);
		testChooser.addObject("PegVisionTest", 50);
		testChooser.addObject("GearVisionTest", 60);
		testChooser.addObject("PegVisionCalibrate", 70);
		testChooser.addObject("GearVisionCalibrate", 80);
		SmartDashboard.putData("Test Mode Chooser", testChooser);
		initialized = true;
	}
	
	public static void testInit() {
		testSelected = testChooser.getSelected();
	}
	
	public static void periodic() {
		switch(testSelected) {
		case 10:
			Auton.stayStill();
			Intake.intakeOff();
			Intake.flipperUp();
			break;
		case 15:
			Auton.stayStill();
			Intake.intakeOff();
			Intake.flipperBack();
			break;
		case 20:
			Auton.stayStill();
			Intake.intakeOff();
			Intake.flipperDown();
			break;
		case 30:
			DriveBase.driver(-0.2, -0.2);
			break;
		case 40:
			DriveBase.driver(0.2, 0.2);
			break;
		case 50:
			break;
		case 60:
			break;
		case 70:
			Auton.stayStill();
			Vision.setCameraPeg();
			Vision.setPegTargetMode();
			testSelected = 100;
			break;
		case 80:
			Auton.stayStill();
			Vision.setCameraGear();
			Vision.setGearTargetMode();
			testSelected = 100;
			break;
		case 100:
			Auton.stayStill();
			break;
		default:
			break;
		}
		
		Intake.updateFlipperPosition();
	}
}
