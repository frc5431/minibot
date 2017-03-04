package org.usfirst.frc.team5431.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;
import edu.wpi.first.wpilibj.Counter;

public class Intake {
	CANTalon intakeMotor;
    CANTalon flipper;
    AnalogTrigger trigger;
    AnalogTriggerType type;
    Counter counter;
    
    public Intake(){
    	trigger = new AnalogTrigger(1);
    	counter = new Counter();
    	counter.setUpSource(trigger,type);
    	counter.setUpDownCounterMode();
    	counter.startLiveWindowMode();
    	
    	intakeMotor = new CANTalon(constants.Intake);
    	flipper = new CANTalon(constants.Flipper);
    }
    
    public void climb(){
    	//int state = 0;
    	//switch(state)
    }
    
    public int setFlipperPosition(){
    	return counter.get();
    }
    
    public void setIntakeSpeed(float speed){
    	flipper.set(speed);
    }
    
}
