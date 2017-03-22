package org.usfirst.frc.team5431.utils;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

public class TitanNavx extends AHRS {
	
	private double absoluteReset = 0;
	private boolean yawDirection = false;
	
	public TitanNavx() {
		super(SPI.Port.kMXP);
		
		this.reset();
		this.resetDisplacement();
	}
	
	public void resetYaw() {
		double tempYaw = this.getYaw();
		
		absoluteReset = tempYaw;
		if(tempYaw <= 0) {
			yawDirection = false; //False == left
		} else {
			yawDirection = true; //True == right
		}
	}
	
	public double getAbsoluteYaw() {
		if(!yawDirection) {
			return this.getYaw() + absoluteReset;
		} else {
			return this.getYaw() - absoluteReset;
		}
	}
}
