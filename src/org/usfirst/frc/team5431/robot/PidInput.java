package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PidInput implements PIDSource{
	PIDSourceType filler = PIDSourceType.kDisplacement;
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		filler = pidSource;
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return filler;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return DriveBase.leftEncoder()-DriveBase.rightEncoder();
	}

}
