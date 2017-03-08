package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePID {

	public static class DriveInputSource implements PIDSource {

		PIDSourceType filler = PIDSourceType.kDisplacement;
		//private int encoderSide;
		//private double totalDriveDistance, wantedSpeed;
		
		
		/*public DriveInputSource(int encoderSide) {
			//this.encoderSide = encoderSide;
			//this.totalDriveDistance = 0;
		}
		
		public void setDriveDistance(double distance) {
			//this.totalDriveDistance = distance;
		}
		
		public void setWantedSpeed(double speed) {
			//this.wantedSpeed = speed;
		}*/
		
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			this.filler = pidSource;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return this.filler;
		}

		@Override
		public double pidGet() {
			/*double distance_left = this.totalDriveDistance;
			
			switch(this.encoderSide) {
			case 1:
				distance_left = DriveBase.leftEncoder();
				break;
			case 2:
				distance_left = DriveBase.rightEncoder();
				break;
			}*/
			
			double differential = DriveBase.leftEncoder() - DriveBase.rightEncoder();
			
			
			
			return differential;
		}
		
	}
	
	public static class DriveOutputSource implements PIDOutput {

		//private double wantedSpeed;
		//private int encoderSide;
		
		/*public DriveOutputSource(int encoderSide) {
			//this.encoderSide = encoderSide;
		}
		
		public void setWantedSpeed(double wantedSpeed) {
			this.wantedSpeed = wantedSpeed;
		}*/
		
		@Override
		public void pidWrite(double output) {
			
			/*if(output > -0.15) {
				output = -0.15;
			} else if(output < 0.15) {
				output = 0.15;
			}*/
			/*double distance_left = this.wantedSpeed;
			
			switch(this.encoderSide) {
			case 1:
				distance_left = DriveBase.leftEncoder();
				break;
			case 2:
				distance_left = DriveBase.rightEncoder();
				break;
			}*/
			
			
			DriveBase.driver(-output,-output);
			SmartDashboard.putNumber("Output PID value", output);
		}
		
	}

}
