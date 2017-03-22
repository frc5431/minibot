package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DriveBasePIDSource implements PIDSource {
	PIDSourceType filler = PIDSourceType.kDisplacement;
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		filler = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return filler;
	}

	@Override
	public double pidGet() {
		return DriveBase.getYaw();
	}

}
