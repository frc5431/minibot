package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DriveBasePIDOutput implements PIDOutput {
	public static double wantedPower = 0.0;
	public static enum PIDType{
		DriveForward, Turn;
	}
	
	public static PIDType wantedPID = PIDType.DriveForward;
	public static double wantedAngle = 0.0, stallPower = 0.22;
	public static boolean drivePID = false;
	
	@Override
	public void pidWrite(double output) {
		if(!drivePID) return;
		
		output = -output;
		
		if(wantedPID == PIDType.DriveForward) {
			if(output > 0) {
				DriveBase.driver(wantedPower, wantedPower - output);
			} else {
				DriveBase.driver(wantedPower + output, wantedPower);
			}
		} else if(wantedPID == PIDType.Turn) {
			/*if(output > -stallPower && output < stallPower) {
				double ratio = ((Math.abs(output) * stallPower) + stallPower) / 2;
				if(output < 0) output = -ratio; else output = ratio;
			}*/
			
			if(wantedAngle < 0) {
				DriveBase.driver(-output, output);
			} else {
				DriveBase.driver(output, -output);
			}
		}
	}

}
