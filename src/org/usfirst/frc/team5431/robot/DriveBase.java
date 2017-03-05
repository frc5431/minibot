package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.constants;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import com.kauailabs.navx.frc.AHRS;
import com.ctre.CANTalon;

public class DriveBase{
	CANTalon bRight;
    CANTalon bLeft;
    CANTalon masterRight;
    CANTalon masterLeft;
    Encoder rightEncoder;
    Encoder leftEncoder;
    AHRS ahrs;
    
	public DriveBase(){
		masterRight = new CANTalon(constants.masterRightId);
//		masterRight.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	masterLeft = new CANTalon(constants.masterLeftId);
//    	masterLeft.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	bRight = new CANTalon(constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(constants.masterRightId);
    	bLeft = new CANTalon(constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(constants.masterLeftId);
    	rightEncoder = new Encoder(0, 1, false, EncodingType.k4X);
    	rightEncoder.setDistancePerPulse(0.00589294059473);
    	leftEncoder = new Encoder(2, 3, false, EncodingType.k4X);
    	leftEncoder.setDistancePerPulse(0.00589294059473);
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
		return masterLeft.get();
	}
	
	public double rightEncoder()
	{
		return masterRight.get();
	}
	
	public double getYaw()
	{
		return ahrs.getYaw();
	}
}
