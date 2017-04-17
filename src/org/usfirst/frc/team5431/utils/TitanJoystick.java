package org.usfirst.frc.team5431.utils;

import edu.wpi.first.wpilibj.Joystick;

public class TitanJoystick extends Joystick {
	private double deadZone = 0.0;
	
	public TitanJoystick(int id) {
		super(id);
	}
	
	public void setDeadZone(double deadZone) {
		this.deadZone = deadZone;
	}
	
	public double getLeftY() {
		return deadZoneHandle(this.getRawAxis(1));
	}
	
	public double getRightY() {
		return deadZoneHandle(this.getRawAxis(5));
	}
	
	public boolean getAButton() {
		return this.getRawButton(1);
	}
	
	public boolean getXButton() {
		return this.getRawButton(3);
	}
	
	public boolean getYButton() {
		return this.getRawButton(4);
	}
	
	public boolean getBButton() {
		return this.getRawButton(2);
	}
	
	public boolean getBackButton() {
		return this.getRawButton(7);
	}
	
	public boolean getLeftBumper() {
		return this.getRawButton(5);
	}
	
	public boolean getRightBumper() {
		return this.getRawButton(6);
	}
	
	public boolean getLeftTrigger() {
		return this.getRawAxis(2) > 0.5;
	}
	
	public boolean getRightTrigger() {
		return this.getRawAxis(3) > 0.5;
	}
	
	private double deadZoneHandle(double value) {
		return (Math.abs(value) > Math.abs(this.deadZone)) ? value : 0.0;
	}
}
;