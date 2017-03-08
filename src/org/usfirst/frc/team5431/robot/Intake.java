package org.usfirst.frc.team5431.robot;
import edu.wpi.first.wpilibj.Timer;


import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
	static CANTalon intakeMotor;
    static CANTalon flipper;
    static CANTalon climber;
    static DigitalInput limit;
    static Potentiometer pot;
    
    public static void intakeInit(){
    	limit = new DigitalInput(4);
    	pot = new AnalogPotentiometer(0,360,30);
    	intakeMotor = new CANTalon(constants.Intake);
    	flipper = new CANTalon(constants.Flipper);
    	climber = new CANTalon(constants.Climber);
    }
    
    public static void climb(){
    	climber.set(-1);
    }
    
    public static void setFlipperPosition(int degree){
    	SmartDashboard.putNumber("pot(decrimalized)", pot.get());	
    	
    }
    
    public static void flipperDown(){
    	flipper.set(1);
    }
    
    public static void flipperUp(){
    	flipper.set(-1);
    }
    
    public static void flipperOff(){
    	flipper.set(0);
    }
    
    public static void intakeOn(){
    	intakeMotor.set(-1);
    	climber.set(-1);
    }
    
    public static void intakeRev(){
    	intakeMotor.set(1);
    }
    
    public static void intakeOff(){
    	intakeMotor.set(0);
    	climber.set(0);
    }
    
    
    public static void upAndOff(){
    	flipper.set(0.5);
    	intakeMotor.set(0.5);
    }
    
    public static void placeGear(){
    	flipper.set(0.5);
    	Timer.delay(0.8);
    	intakeMotor.set(1);
    	Timer.delay(0.8);
    	SmartDashboard.putNumber("did delay work", 1);
    	intakeOff();
    }
    
    public static boolean isLimit(){
    	return !limit.get();
    }
    
    
    public static void toggleIntake(){
    	if (isIntakeOn()){
    		intakeOff();
    	}
    	else{
    		intakeOn();
    	}
    }
    
    public static boolean isIntakeOn(){
    	if (intakeMotor.get() < -0.1){ //inverted motor
    		return true;
    	}
    	else{
        	return false;
    	}
    }
    
    public static boolean isClimberOn(){
    	if (climber.get() < -0.1){ //inverted motor
    		return true;
    	}
    	else{
        	return false;
    	}
    }
}
