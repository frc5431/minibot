package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.perception.Vision;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DriveBasePIDSource implements PIDSource {
	public static InputType inputType = InputType.Navx;
	
	PIDSourceType filler = PIDSourceType.kDisplacement;
	
	public static enum InputType {
		Navx, Vision
	}
	
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
		if(inputType == InputType.Navx) {
			return DriveBase.getYaw();
		} else if(inputType == InputType.Vision){
			return -(Vision.getTargetAngle());
		} else {
			return 0.0;
		}
	}

}
