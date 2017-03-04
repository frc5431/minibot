package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Intake {
	CANTalon intakeMotor;
    CANTalon flipper;
    Potentiometer pot;
    
    public Intake(){
    	pot = new AnalogPotentiometer(0,360,30);
    	intakeMotor = new CANTalon(constants.Intake);
    	flipper = new CANTalon(constants.Flipper);
    }
    
    public void climb(){
    	
    }
    
    public void setFlipperPosition(int degree){
    	pot.get();	
    }
    
    public void flipperUp(){
    	
    }
    
    public void flipperDown(){
    	
    }
    
    public void intakeOn(float speed){
    	intakeMotor.set(speed);
    }
    
    public void intakeOff(){
    	intakeMotor.set(0);
    }
    
}
