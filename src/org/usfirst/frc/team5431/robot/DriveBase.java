package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.Constants;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    public static boolean isVision = false;
    
    public static double 
    	driveP, driveI, driveD,
    	visionP, visionI, visionD,
    	turnP, turnI, turnD;
    
	public static void init(){
		masterRight = new CANTalon(Constants.masterRightId);
		masterRight.enableBrakeMode(true);
    	masterLeft = new CANTalon(Constants.masterLeftId);
		masterLeft.enableBrakeMode(true);
    	bRight = new CANTalon(Constants.bRightId);
    	bRight.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bRight.set(Constants.masterRightId);
		bRight.enableBrakeMode(true);

    	bLeft = new CANTalon(Constants.bLeftId);
    	bLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
    	bLeft.set(Constants.masterLeftId);
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
    	
    	masterLeft.setVoltageRampRate(0);
    	masterRight.setVoltageRampRate(0);
    	
    	if(Constants.isType(RobotType.Practice)) {
        	driveP = Constants.PracticeBot.driveP;
        	driveI = Constants.PracticeBot.driveI;
        	driveD = Constants.PracticeBot.driveD;
        	visionP = Constants.PracticeBot.visionP;
        	visionI = Constants.PracticeBot.visionI;
        	visionD = Constants.PracticeBot.visionD;
        	turnP = Constants.PracticeBot.turnP;
        	turnI = Constants.PracticeBot.turnI;
        	turnD = Constants.PracticeBot.turnD;
    	} else {
        	driveP = Constants.CompetitionBot.driveP;
        	driveI = Constants.CompetitionBot.driveI;
        	driveD = Constants.CompetitionBot.driveD;
        	visionP = Constants.CompetitionBot.visionP;
        	visionI = Constants.CompetitionBot.visionI;
        	visionD = Constants.CompetitionBot.visionD;
        	turnP = Constants.CompetitionBot.turnP;
        	turnI = Constants.CompetitionBot.turnI;
        	turnD = Constants.CompetitionBot.turnD;
    	}
    	
    	isPID = false;
	}
	
	public static void setPIDNormal() {
		isVision = false;
	}
	
	public static void setPIDVision() {
		isVision = true;
	}
	
	public static void enablePID() {
		drivePID.enable();
		isPID = true;
	}
	
	public static void drivePIDBackward(double power) {
		drivePIDBackward(power, 0);
	}
	
	public static void drivePIDBackward(double power, double setpoint) {
		driver(power, power);
		drivePID.reset();
		drivePID.setSetpoint(0);
		if(isVision) {
			drivePID.setPID(visionP, visionI, visionD, 0.0);
			SmartDashboard.putBoolean("VisionPID", true);
		} else {
			drivePID.setPID(driveP, driveI, driveD, 0.0);
			SmartDashboard.putBoolean("VisionPID", false);
		}
		setDrivePIDSpeed(-power);
		DriveBasePIDOutput.wantedPID = DriveBasePIDOutput.PIDType.DriveForward;
    	drivePID.setInputRange(-90, 90);
    	drivePID.setOutputRange(-1, 1);
    	drivePID.setAbsoluteTolerance(0.5);
    	drivePID.setToleranceBuffer(8);
    	drivePID.setContinuous(true);
		drivePID.setSetpoint(setpoint);
		enablePID();
	}
	
	public static void drivePIDForward(double power) {
		drivePIDForward(power, 0);
	}
	
	public static void drivePIDForward(double power, double setpoint) {
		driver(-power, -power);
		drivePID.reset();
		drivePID.setSetpoint(0);
		if(isVision) {
			drivePID.setPID(visionP, visionI, visionD, 0.0);
			SmartDashboard.putBoolean("VisionPID", true);
		} else {
			drivePID.setPID(driveP, driveI, driveD, 0.0);
			SmartDashboard.putBoolean("VisionPID", false);
		}
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
		if(right > 0.08 || right < -0.08){
			masterRight.set(right);
		}
		else{
			masterRight.set(0);
		}
		
		if(left > 0.08 || left < -0.08){
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
	
	public static void reset() {
		resetEncoders();
		resetAHRS();
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
