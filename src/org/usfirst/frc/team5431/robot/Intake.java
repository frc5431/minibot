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
    static CANTalon climberSlave;
    static DigitalInput limit;
//    static Potentiometer pot;
    static AnalogPotentiometer pot;
    static AnalogTrigger analogTrigger;
    static Counter halCount;
    static double speedPrevious;
    static int position;
    static int flipperUp;
    //static DigitalInput flipperLimit;
    
    public static void intakeInit(){
    	//flipperLimit = new DigitalInput(5);
    	limit = new DigitalInput(4);
    	pot = new AnalogPotentiometer(0,270,0);
    	intakeMotor = new CANTalon(Constants.Intake);
    	flipper = new CANTalon(Constants.Flipper);
    	flipper.enableLimitSwitch(true, true);
    	climber = new CANTalon(Constants.ClimberMaster);
    	climber.setInverted(true); 
    	climber.enableBrakeMode(false);
    	climberSlave = new CANTalon(Constants.ClimberSlave);
    	climberSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
    	climberSlave.enableBrakeMode(false);
    	climberSlave.set(climber.getDeviceID());
    	analogTrigger = new AnalogTrigger(1);
    	
    	analogTrigger.setLimitsVoltage(3.5, 3.8);
    	halCount = new Counter(analogTrigger);
    	speedPrevious = 0.0f;
    	position = 0;
    }
    
    public static void climbSlow() {
    	climber.set(-0.45);
    }
    
    public static void climb() {
    	climber.set(-1);
    }
    
    public static void setFlipperPosition(int degree){
    	SmartDashboard.putNumber("pot(decrimalized)", pot.get());	
    	
    }
    
    public static int getPosition()
    {
    	if(speedPrevious >= 0)
    		return position + halCount.get();
    	return position - halCount.get();
    }
    
    public static double checkDirectionChange(double newSpeed)
    {
    	if((speedPrevious < 0 && newSpeed >= 0) || (speedPrevious >= 0 && newSpeed < 0))
    	{
    		position = getPosition();
    		halCount.reset();
    		speedPrevious = newSpeed;
    	}
    	return newSpeed;
    }
    
    public static boolean getFlipperUpLimit() {
    	return flipper.isFwdLimitSwitchClosed();
    }
    
    public static boolean getFlipperDownLimit() {
    	return flipper.isRevLimitSwitchClosed();
    }
    
    public static void rebaseIntake() {
    	Intake.flipperBack();
    	for(int a = 0; a < 3000; a++) {
    		if(Intake.getFlipperUpLimit()) break;
    		Intake.flipper.set(1);
    		Timer.delay(0.05);
    	}
    }
    
    public static void updateFlipperPosition()
    {
    	boolean flipperUpLimit = (boolean) getFlipperUpLimit();
    	SmartDashboard.putBoolean("flipper", flipperUpLimit);
    	SmartDashboard.putNumber("flipperPos", flipperUp);
//    	if(!flipperLimit.get() && flipperUp == 2)
//    	{
//    		position = 0;
//    		halCount.reset();
//    		flipper.set(checkDirectionChange(0));
//    	}
//    	else
//    	{
//	    	int mPos = getPosition();
//	    	boolean turnOff = false;
//	    	if(flipperUp == 1)//If flipper should be up 50
//	    	{
//	    		if(mPos >= -40)
//	    			flipper.set(checkDirectionChange(1));
//	    		else if(mPos <= -65)
//	    			flipper.set(checkDirectionChange(-1));
//	    		else turnOff = true;
//	    	}
//	    	else if(flipperUp == 0)//-40
//	    	{
//	    		if(mPos >= -65)
//	    			flipper.set(checkDirectionChange(-1));
//	    		else turnOff = true;
//	    	}
//	    	else if(flipperUp == 2)
//	    	{
//	    		if(mPos >= -85)
//	    			flipper.set(checkDirectionChange(1));
//	    		else turnOff = true;
//	    	}
//	    	else
//	    		flipper.set(checkDirectionChange(0));
//	    	
//	    	if(turnOff) flipper.set(checkDirectionChange(0));
//    	}
    	int mPos = getPosition();
    	if(flipperUp == 2)
    	{
    		if(!flipperUpLimit)
    			flipper.set(checkDirectionChange(1));
    		else
    		{
    			flipper.set(0);
    			halCount.reset();
    			position = 0;
    		}
    	}
    	else if(flipperUp == 1)
    	{
    		if(mPos >= -33)
    			flipper.set(checkDirectionChange(-1));
    		else if(mPos <= -42)
    			flipper.set(checkDirectionChange(1));
    		else {
    			if(mPos >= -35) {
    				flipper.set(checkDirectionChange(-0.1));
    			} else if(mPos <= -39) {
    				flipper.set(checkDirectionChange(0.1));
    			} else {
    				flipper.set(0);
    			}
    		}
    	}
    	else if(flipperUp == 0)
    	{
    		if(!getFlipperDownLimit()) { //mPos >= 68 || 
    			//mPos = -70;
    			flipper.set(checkDirectionChange(-1));
    		}
    		else {
    			mPos = -70;
    			flipper.set(0);
    		}
    	}
    	
    	SmartDashboard.putNumber("Flipper hal position", mPos);
    	
    }
    
    public static void flipperDown(){
    	//if(flipperUp != 0)
    		flipperUp = 0;
    }
    
    public static void flipperUp(){
    	//if(flipperUp != 1)
    		flipperUp = 1;
    }
    
    public static void flipperBack(){
    	//if(flipperUp != 2)
    		flipperUp = 2;
    }
    
    public static void flipperOff(){
    	flipper.set(0);
    }
    
    public static void intakeOn(){
    	intakeMotor.set(1);
    	//climber.set(-1);
    }
    
    public static void intakeRev(){
    	intakeMotor.set(-1);
    }
    
    public static void intakeOff(){
    	intakeMotor.set(0);
    	//climber.set(0);
    }
    
    public static void climbOff() {
    	climber.set(0);
    }
    
    
    public static void upAndOff(){
    	flipper.set(-0.5);
    	Timer.delay(1.3);
    	//intakeMotor.set(0.5);
    }
    
    public static void placeGear(){
    	flipper.set(-0.6);
    	Timer.delay(1);
    	intakeMotor.set(1);
    	Timer.delay(0.25); //Timer delay (0.2 seconds)
    	flipper.set(0); //Remove
    	Timer.delay(0.75); //Remove
    	SmartDashboard.putNumber("did delay work", 1);
    	intakeOff();
    }
    
    public static void outGear() {
    	intakeMotor.set(1);
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
    	if (intakeMotor.get() > 0.1){ //inverted motor
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
