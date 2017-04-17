package org.usfirst.frc.team5431.utils;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TitanDrive {
	
	private TitanTalon frontRight, frontLeft, rearRight, rearLeft;
	//private TitanEncoder leftSide, rightSide;
	private DriveController driveController = null;
	private double turnBand, currentAngle, wantedAngle, previousTurn, leftOffset, rightOffset, driveBand, slopeHandler, offsetHandler;
	private long resetCounter, resetCounterLimit;
	
	public interface DriveController {
		public double getGlobalAngle();
		public void resetGlobalAngle();
	}
	
	public TitanDrive(TitanTalon fl, TitanTalon fr, TitanTalon rl, TitanTalon rr) {
		this.frontLeft = fl;
		this.frontRight = fr;
		this.rearLeft = rl;
		this.rearRight = rr;
		
		this.rearLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
		this.rearRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		this.rearLeft.set(this.frontLeft.getDeviceID());
		this.rearRight.set(this.frontRight.getDeviceID());
		
		this.driveBand = 0.2;
		this.turnBand = 0.2;
		this.currentAngle = 0.0;
		this.wantedAngle = 0.0;
		this.previousTurn = 0.0;
		this.leftOffset = 0.0;
		this.rightOffset = 0.0;
		this.slopeHandler = 0.85;
		this.offsetHandler = 0.15;
		this.resetCounter = 0;
		this.resetCounterLimit = 100;
		
		this.setBrakeMode(true);
	}
	
	public void setDriveController(DriveController dc) {
		this.driveController = dc;
	}
	
	public void setTurnBand(double band) {
		this.turnBand = band;
	}
	
	public void setDriveBand(double band) {
		this.driveBand = band;
	}
	
	public void setSlopeHandler(double slope, double offset) {
		this.slopeHandler = slope;
		this.offsetHandler = offset;
	}
	
	public void setResetCountLimit(long limit) {
		this.resetCounterLimit = limit;
	}
	
	public void setBrakeMode(boolean state) {
		this.frontLeft.enableBrakeMode(state);
		this.frontRight.enableBrakeMode(state);
		this.rearLeft.enableBrakeMode(state);
		this.rearRight.enableBrakeMode(state);
	}
	
	private void homebrewDrive(double wantedPower, double angleWanted) {
		double distanceDiff = angleWanted / 2;
		double diffRatio = 0.1;
		double newPower = (distanceDiff * diffRatio) / 3;
		if(newPower > 0) {
			this.leftOffset = wantedPower;
			this.rightOffset = wantedPower - newPower;
			this.drive(this.leftOffset, this.rightOffset);
		} else {
			this.leftOffset = wantedPower + newPower;
			this.rightOffset = wantedPower;
			this.drive(this.leftOffset, this.rightOffset);
		}
	}
	
	private double deadBandHandle(double value, double deadBand) {
		return (Math.abs(value) > Math.abs(deadBand)) ? value : 0.0;
	}
	
	private boolean withinBand(double value, double deadBand) {
		return (Math.abs(value) <= Math.abs(deadBand));
	}
	
	private double getSlopeValue(double value) {
		return (this.slopeHandler * Math.pow(value, 3)) + (this.offsetHandler * value);
	}
	
	public void updateGlobalAngle() {
		if(this.driveController == null) return;
		this.currentAngle = this.driveController.getGlobalAngle();
	}
	
	public void resetGlobalAngle() {
		if(this.driveController == null) return;
		this.driveController.resetGlobalAngle();
	}
	
	public double getGlobalAngle() {
		return this.currentAngle;
	}
	
	private double[] turnHandler(double throttle, double turn) {
		double driveH[] = new double[2];
		
		double throttleL = (Math.abs(throttle - this.leftOffset) > 0.1)  ? throttle : (throttle + (this.leftOffset / 20));
		double throttleR = (Math.abs(throttle - this.rightOffset) > 0.1) ? throttle : (throttle + (this.rightOffset / 20));
		
		if(throttle > 0.0) {
			if(turn > 0.0) {
				driveH[0] = throttleL - turn;
				driveH[1] = Math.max(throttleR, turn);
			} else {
				driveH[0] = Math.max(throttleL, -turn);
				driveH[1] = throttleR + turn;
			}
		} else {
			if(turn > 0.0) {
				driveH[0] = -Math.max(-throttleL, turn);
				driveH[1] = throttleR + turn;
			} else {
				driveH[0] = throttleL - turn;
				driveH[1] = -Math.max(-throttleR, -turn);
			}
		}
		return driveH;
	}
	
	public void drive(double leftSide, double rightSide) {
		frontLeft.setSpeed(-leftSide);
		frontRight.setSpeed(rightSide);
	}
	
	public void standStill() {
		this.drive(0, 0);
	}
	
	public void titanDrive(double throttle, double turn) {
		try {
			this.updateGlobalAngle();
			throttle = this.getSlopeValue(this.deadBandHandle(throttle, this.driveBand));
			//boolean holdLast = false;
			
			//Periodically reset the angle back to what it is currently if the turn is at 0
			/*if(this.resetCounter++ > this.resetCounterLimit && withinBand(turn, this.turnBand)) {
				//this.resetGlobalAngle();
				//this.updateGlobalAngle();
				this.wantedAngle = this.currentAngle;
				this.resetCounter = 0;
				holdLast = true;
			}*/
			if(withinBand(turn, this.turnBand)) { // && !withinBand(this.previousTurn, this.turnBand)) {
				//We want it to drive straight (Set the new angle)
				this.resetGlobalAngle();
				this.updateGlobalAngle();
				this.wantedAngle = 0; //currentAngle;
			}
			
			if(withinBand(throttle, 0.1) && withinBand(turn, 0.01)) {
				this.standStill(); 
				this.previousTurn = turn;
				this.resetGlobalAngle();
				this.updateGlobalAngle();
				this.wantedAngle = 0; //this.currentAngle; 
				return;
			}
			
			SmartDashboard.putNumber("WANTEDANGLE", this.wantedAngle);
			SmartDashboard.putNumber("GETTINGANGLE", this.currentAngle);
			SmartDashboard.putNumber("TURNVALUE", turn);
			
			if(withinBand(turn, this.turnBand)) {
				//if(!holdLast) {
					SmartDashboard.putBoolean("HOMEBREW", true);
					homebrewDrive(throttle, this.currentAngle); //this.currentAngle - this.wantedAngle);//this.wantedAngle);
				//} else {
					//this.drive(this.leftOffset, this.rightOffset);
				//}
			} else {
				SmartDashboard.putBoolean("HOMEBREW", false);
				turn = this.getSlopeValue(turn);
				double dDrive[] = turnHandler(throttle, turn);
				this.drive(dDrive[0], dDrive[1]);
			}
		} catch(Throwable error) {
			error.printStackTrace();
			SmartDashboard.putString("ERROR", "titanDrive");
		}
		this.previousTurn = turn;
	}	
}
 	