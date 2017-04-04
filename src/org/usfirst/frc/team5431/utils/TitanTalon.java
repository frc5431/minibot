/*package org.usfirst.frc.team5431.utils;

import com.ctre.CANTalon;

public class TitanTalon extends CANTalon {
	
	private double deadZone = 0;
	
	public TitanTalon(int id) {
		super(id);
	}
	
	public void setDeadZone(double range) {
		this.deadZone = range;
	}
	
	public void setSpeed(double speed) {
		this.set((Math.abs(speed) < Math.abs(deadZone)) ? 0 : speed);
	}
	
	public double getDeadZone() {
		return this.deadZone;
	}
}
*/