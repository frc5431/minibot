package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePID implements PIDOutput{
	PIDController driveController;
	PidInput input;
	
	public DrivePID() {
		input = new PidInput();
		driveController = new PIDController(0.02, 0, 0, input, this);
		driveController.setInputRange(-1000, 1000);
		driveController.setOutputRange(-0.2, 0.2);
		driveController.setAbsoluteTolerance(2);
		driveController.setContinuous(true);
		driveController.setSetpoint(0);
	}
	
	@Override
	public void pidWrite(double output) {
		System.out.print(output);
		DriveBase.driver(-0.35+output, -0.35-output);
	}
	
}
