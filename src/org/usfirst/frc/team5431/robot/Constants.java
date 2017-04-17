package org.usfirst.frc.team5431.robot;

public class Constants {
    public static final RobotType type = RobotType.Practice;
	
    public static RobotType getType() {
    	return type;
    }
    
    public static boolean isType(RobotType otherType) {
    	return type == otherType;
    }
    
    
	public static final int masterLeftId = 5;
    public static final int masterRightId = 2;
    public static final int bLeftId = 4;
    public static final int bRightId = 3;
    public static final int Intake = 7;
    public static final int Flipper = 1;
    public static final int ClimberMaster = 8;  //real id is 6
    public static final int ClimberSlave = 6;
    
    public static final class Auton {
    	public static final double 
    				visionFindingPower = 0.2, //Speed to travel while looking for target
    				visionFoundPower = 0.195, //Speed to travel when target is found
    				visionFoundGearPower = 0.22, //Speed to travel when gear is found
    				
    				driveForwardPower = 0.25,
    				driveBackwardPower = 0.3,
    				driveTurnPower = 0.18;
    }
    
    public static final class Vision {
    	public static final int
    				imageWidth = 160,
    				imageHeight = 120;
    	
    	public static final double
    				cameraFov = 50.466,
    				degreesPerPixel = cameraFov / (double) imageWidth,
    				cameraOffset = -1.5; //Add to camera misspositioning
    	
    	public static double fromCenter(double pixel) {
    		return pixel - (imageWidth / 2);
    	}
    	
    	public static double angleFromCenter(double center) {
    		return center * degreesPerPixel;
    	}
    }
    
    public static final class Joystick {
    	public static final int 
    				driverId = 0, //Joystick id for the driver xbox controller
    				operatorId = 1; //Joystick id for the operator xbox controller
    	public static final double
    				deadZone = 0.1; //Joystick drive deadzone
    }
    
    
    public static final class PracticeBot {
        public static final double
        	driveP = 0.010,
        	driveI = 0.0008,
        	driveD = 0.00035,
        	visionP = 0.015,
        	visionI = 0.00004,
        	visionD = 0.00040,
        	turnP = 0.14,
        	turnI = 0.0021,
        	turnD = 0.00051;
    }
    
    public static final class CompetitionBot {
        public static final double
	    	driveP = 0.022,
	    	driveI = 0.0012,
	    	driveD = 0.00031,
	    	visionP = 0.015,
	    	visionI = 0.00004,
	    	visionD = 0.00040,
	    	turnP = 0.14,
	    	turnI = 0.0021,
	    	turnD = 0.00051;
    }
    
}
