package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.constants;
import com.ctre.CANTalon;

public class DriveBase{
	CANTalon bRight;
    CANTalon bLeft;
    CANTalon masterRight;
    CANTalon masterLeft;
    
	public DriveBase(){
		masterRight = new CANTalon(constants.masterRightId);
    	masterLeft = new CANTalon(constants.masterLeftId);
    	bRight = new CANTalon(constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(constants.masterRightId);
    	bLeft = new CANTalon(constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(constants.masterLeftId);
	}
	
	public void setLeftRight(double left, double right){
		masterRight.set(right);
    	masterLeft.set(-left); 
	}
}
