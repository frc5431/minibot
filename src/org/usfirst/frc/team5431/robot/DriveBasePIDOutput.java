package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DriveBasePIDOutput implements PIDOutput {
	public static double wantedPower = 0.0;
	
	@Override
	public void pidWrite(double output) {
		output = -output;
		if(output > 0) {
			DriveBase.driver(wantedPower, wantedPower - output);
		} else {
			DriveBase.driver(wantedPower + output, wantedPower);
		}
	}

}
