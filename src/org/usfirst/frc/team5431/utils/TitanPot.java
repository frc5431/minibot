package org.usfirst.frc.team5431.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class TitanPot {
	
	private AnalogInput pot;
	private double minAngle = 0, maxAngle = 180;
	private double minPotValue = 0, maxPotValue = 4096;
	private double absoluteReset = 0;
	private boolean potDirection = false;
	
	public TitanPot(int analogPort) {
		this.pot = new AnalogInput(analogPort);
	}
	
	public void setMinMaxAngle(double minAngle, double maxAngle) {
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}
	
	public void setMinMaxPot(double minPotValue, double maxPotValue) {
		this.minPotValue = minPotValue;
		this.maxPotValue = maxPotValue;
	}
	
	public void resetAngle() {
		absoluteReset = this.getAbsoluteAngle();
		potDirection = (absoluteReset > 0); //False == less, True == more
	}
	
	public double getAngle() {
		double currentAngle = this.getAbsoluteAngle();
		return (potDirection) ? currentAngle - absoluteReset : currentAngle + absoluteReset;
	}
	
	public double getAbsoluteAngle() {
		return linearMap(this.pot.getValue(), this.minPotValue, this.maxPotValue, this.minAngle, this.maxAngle);
	}
	
	public static double linearMap(double currentValue, double minInputValue, double maxInputValue, double minOutputValue, double maxOutputValue) {
		return (currentValue - minInputValue) * (maxOutputValue - minOutputValue) / (minInputValue - maxInputValue) + minOutputValue;
	}
	
	public double getVoltage() {
		return this.pot.getVoltage();
	}
}
