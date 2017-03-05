package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.constants;
import com.kauailabs.navx.frc.AHRS;
import com.ctre.CANTalon;

public class DriveBase{
	CANTalon bRight;
    CANTalon bLeft;
    CANTalon masterRight;
    CANTalon masterLeft;
    AHRS ahrs;
    
	public DriveBase(){
		masterRight = new CANTalon(constants.masterRightId);
		masterRight.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	masterLeft = new CANTalon(constants.masterLeftId);
    	masterLeft.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	bRight = new CANTalon(constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(constants.masterRightId);
    	bLeft = new CANTalon(constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(constants.masterLeftId);
	}
	
	public void drive(double left, double right){
		if(right > 0.1 || right < -0.1){
			masterRight.set(right);
		}
		if(left > 0.1 || left < -0.1){
	    	masterLeft.set(-left); 
		}
	}
	
	public void resetEncoders()
	{
		masterRight.reset();
		masterLeft.reset();
	}
	
	public double leftEncoder()
	{
		return masterLeft.getEncPosition() * 0.00589294059473;
	}
	
	public double rightEncoder()
	{
		return masterRight.getEncPosition() * 0.00589294059473;
	}
	
	public double getYaw()
	{
		return ahrs.getYaw();
	}
}
