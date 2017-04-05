package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.constants;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.CANTalon;

public class DriveBase{
	static CANTalon bRight;
    static CANTalon bLeft;
    static CANTalon masterRight;
    static CANTalon masterLeft;
    static Encoder rightEncoder;
    static Encoder leftEncoder;
    static AHRS ahrs;
    static PIDController drivePID;
    static DriveBasePIDSource pidSource;
    static DriveBasePIDOutput pidOutput;
    static boolean isPID = false;
    
    public static final boolean isComp = false;
    
    public static final double
    	driveP = (isComp) ? 0.022 : 0.010,
    	driveI = (isComp) ? 0.0012 : 0.0008,
    	driveD = (isComp) ? 0.00031 : 0.00035,
    	turnP = 0.14,
    	turnI = 0.0021,
    	turnD = 0.00051;
    
	public static void driveBaseInit(){
		masterRight = new CANTalon(constants.masterRightId);
		masterRight.enableBrakeMode(true);
//		masterRight.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	masterLeft = new CANTalon(constants.masterLeftId);
		masterLeft.enableBrakeMode(true);
//    	masterLeft.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
    	bRight = new CANTalon(constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(constants.masterRightId);
		bRight.enableBrakeMode(true);

    	bLeft = new CANTalon(constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(constants.masterLeftId);
		bLeft.enableBrakeMode(true);

    	rightEncoder = new Encoder(0, 1, false, EncodingType.k4X);
    	rightEncoder.setDistancePerPulse(4  * Math.PI/360);
    	rightEncoder.setSamplesToAverage(1);
    	rightEncoder.setReverseDirection(true);
    	leftEncoder = new Encoder(2, 3, false, EncodingType.k4X);
    	leftEncoder.setDistancePerPulse(4 * Math.PI/360);
    	leftEncoder.setSamplesToAverage(1);
    	
    	ahrs = new AHRS(SPI.Port.kMXP);
    	
    	pidSource = new DriveBasePIDSource();
    	pidOutput = new DriveBasePIDOutput();
    	drivePID = new PIDController(driveP, driveI, driveD, 0.0, pidSource, pidOutput);
    	drivePID.setInputRange(-90, 90);
    	drivePID.setOutputRange(-1, 1);
    	drivePID.setAbsoluteTolerance(0.5);
    	drivePID.setToleranceBuffer(8);
    	drivePID.setContinuous(true);
    	drivePID.disable();
    	
    	isPID = false;
	}
	
	
	public static void enablePID() {
		drivePID.enable();
		isPID = true;
	}
	
	public static void drivePIDForward(double power) {
		drivePIDForward(power, 0);
	}
	
	public static void drivePIDForward(double power, double setpoint) {
		driver(-power, -power);
		drivePID.reset();
		drivePID.setSetpoint(0);
		drivePID.setPID(driveP, driveI, driveD, 0.0);
		setDrivePIDSpeed(power);
		DriveBasePIDOutput.wantedPID = DriveBasePIDOutput.PIDType.DriveForward;
    	drivePID.setInputRange(-90, 90);
    	drivePID.setOutputRange(-1, 1);
    	drivePID.setAbsoluteTolerance(0.5);
    	drivePID.setToleranceBuffer(8);
    	drivePID.setContinuous(true);
		drivePID.setSetpoint(setpoint);
		enablePID();
	}
	
	public static void drivePIDTurn(double angle) {
		drivePID.reset();
		drivePID.setSetpoint(0);
		drivePID.setPID(turnP, turnI, turnD, 0.0);
		setDrivePIDSpeed(0.3);
		DriveBasePIDOutput.wantedAngle = angle;
		DriveBasePIDOutput.wantedPID = DriveBasePIDOutput.PIDType.Turn;
    	drivePID.setInputRange(-90, 90);
    	drivePID.setOutputRange(-0.239, 0.239);
    	drivePID.setAbsoluteTolerance(0.5);
    	drivePID.setToleranceBuffer(4);
    	drivePID.setContinuous(true);
		drivePID.setSetpoint(angle);
		enablePID();
	}
	
	public static void setDrivePIDSpeed(double power) {
		if(power == 0.0) DriveBasePIDOutput.drivePID = false; else DriveBasePIDOutput.drivePID = true;
		DriveBasePIDOutput.wantedPower = -power;
	}
	
	public static void disablePID() {
		drivePID.disable();
		isPID = false;
	}
	
	public static boolean isPID() {
		return isPID;
	}
	
	public static void driver(double left, double right){
		if(right > 0.2 || right < -0.2){
			masterRight.set(right);
		}
		else{
			masterRight.set(0);
		}
		
		if(left > 0.2 || left < -0.2){
	    	masterLeft.set(-left); 
		}
		else{
			masterLeft.set(0);
		}
	}
	
	public static void resetEncoders()
	{
		rightEncoder.reset();
		leftEncoder.reset();
	}
	
	public static double leftEncoder()
	{
		return leftEncoder.getDistance();
	}
	
	public static double rightEncoder()
	{
		return rightEncoder.getDistance();
	}
	
	
	public static double getYaw()
	{
		return ahrs.getYaw();
	}
	
	public static void resetAHRS()
	{
		ahrs.zeroYaw();
		ahrs.resetDisplacement();
		//ahrs.reset();
	}
}
