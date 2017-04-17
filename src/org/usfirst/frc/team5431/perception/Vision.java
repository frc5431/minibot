package org.usfirst.frc.team5431.perception;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5431.robot.Constants;
import org.usfirst.frc.team5431.robot.DriveBase;
import org.usfirst.frc.team5431.robot.DriveBasePIDSource;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	public static GripPipeline grip;
	public static GearPipeline gripGear;
	public static UsbCamera camera;
	public static CvSink cv;
	public static Mat image;
	public static boolean 
			visionTargetFound = false, 
			initialized = false;
	public static double 
			visionAngle = 0, 
			visionDistance = 0;
	
	public static enum TargetMode {
		Normal, PegDetector, GearDetector
	}
	
	public static TargetMode targetting = TargetMode.Normal;
	
	public static void init() {
		if(initialized) return;
		grip = new GripPipeline();
		gripGear = new GearPipeline();
		image = new Mat();
		
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(Constants.Vision.imageWidth, Constants.Vision.imageHeight);
		cv = CameraServer.getInstance().getVideo();
		
		initialized = true;
	}
	
	public static void setCameraNormal() {
		camera.setBrightness(50);
		camera.setExposureManual(30);
	}
	
	public static void setCameraPeg() {
		camera.setBrightness(0);
		camera.setExposureManual(0);
	}
	
	public static void setCameraGear() {
		camera.setBrightness(10);
		camera.setExposureManual(25);
	}
	
    public static Rect getRectangle(MatOfPoint contour) {
    	return Imgproc.boundingRect(contour);
    }
    
    public static double getCenterX(Rect boundingBox) {
    	return boundingBox.x + (boundingBox.width / 2);
    }
    
    public static double getCenterY(Rect boundingBox) {
    	return boundingBox.y + (boundingBox.height / 2);
    }
    
    public static boolean inSameSpot(Rect parent, Rect child) {
    	return (getCenterX(parent) == getCenterX(child)) && (getCenterY(parent) == getCenterY(child));
    }
    
    public static double getArea(MatOfPoint contour) {
    	return Imgproc.contourArea(contour);
    }
	
    public static void processGearFrame() {
    	cv.grabFrame(image);
    	if(image != null)
    	{
    		if(!image.empty()) {
    			gripGear.process(image);
    			List<MatOfPoint> contourPoints = gripGear.filterContoursOutput();
    			
    			if(contourPoints.size() == 0) {
    				visionTargetFound = false;
    				return;
    			}
    			
    			MatOfPoint largestMat = contourPoints.get(0);
    			for(MatOfPoint object : contourPoints) {
    				if(getArea(object) > getArea(largestMat)) {
    					largestMat = object;
    				}
    			}
    			
    			Rect gearRect = getRectangle(largestMat);
    			double centerX = getCenterX(gearRect);
    			double fromCenter = Constants.Vision.fromCenter(centerX);
    			visionAngle = Constants.Vision.angleFromCenter(fromCenter);
    			visionDistance = getCenterY(gearRect);
    			visionTargetFound = true;
    		} else {
    			visionTargetFound = false;
    		}
    	} else {
    		visionTargetFound = false;
    	}
    	
		SmartDashboard.putBoolean("FoundGear", visionTargetFound);
    }
    
    public static void processPegFrame() {
    	cv.grabFrame(image);
    	if(image != null)
    	{
    		if(!image.empty()) {
    			grip.process(image);
    			List<MatOfPoint> contourPoints = grip.filterContoursOutput();
    			
    			Rect leftPeg = new Rect(), rightPeg = new Rect();
    			double centerX = 666;
    			
    			if(contourPoints.size() < 2) {
    				SmartDashboard.putBoolean("ContoursFound", false);
    				visionTargetFound = false;
    				return;
    			}
    			
    			SmartDashboard.putBoolean("ContoursFound", true);
    			
    			for(MatOfPoint parent : contourPoints) {
    				Rect parentBox = getRectangle(parent);
    				double parentX = getCenterX(parentBox);
    				double parentY = getCenterY(parentBox);
    				
    				for(MatOfPoint child : contourPoints) {
    					Rect childBox = getRectangle(child);
    					if(inSameSpot(parentBox, childBox)) continue;
    					double childX = getCenterX(childBox);
    					double childY = getCenterY(childBox);
    					
    					if(Math.abs(parentY - childY) < 10) {
    						SmartDashboard.putBoolean("PegFound", true);
    						centerX = (parentX + childX) / 2;
    						if(parentX < childX) {
    							leftPeg = parentBox;
    							rightPeg = childBox;
    						} else {
    							leftPeg = childBox;
    							rightPeg = parentBox;
    						}
    						visionTargetFound = true;
    						break;
    					} else {
    						SmartDashboard.putBoolean("PegFound", false);
    					}
    				}
    			}
    			
    			double fromCenter = Constants.Vision.fromCenter(centerX);
    			visionAngle = Constants.Vision.angleFromCenter(fromCenter);
    			visionDistance = Math.abs(getCenterX(leftPeg) - getCenterX(rightPeg));
    			
    	    	SmartDashboard.putNumber("PegPairCenterX", fromCenter);
    	    	SmartDashboard.putNumber("PegHorzAngle", visionAngle);
    	    	SmartDashboard.putNumber("PegDisplacement", visionDistance);
    		} else {
    	    	visionTargetFound = false;
    	    }
    	} else {
    		visionTargetFound = false;
    	}
    }
    
    public static void useAngleFromCamera() {
    	DriveBasePIDSource.inputType = DriveBasePIDSource.InputType.Vision;
    }
    
    public static void useAngleFromNavx() {
    	DriveBasePIDSource.inputType = DriveBasePIDSource.InputType.Navx;
    }
    
    public static boolean isOnTarget() {
    	return visionDistance > 43;
    }
    
    public static boolean foundTarget() {
    	return visionTargetFound;
    }
    
    public static double getAngle() {
    	return -(visionAngle + Constants.Vision.cameraOffset);
    }
    
    public static void setGearTargetMode() {
    	targetting = TargetMode.GearDetector;
    }
    
    public static void setPegTargetMode() {
    	targetting = TargetMode.PegDetector;
    }
    
    public static void setNormalTargetMode() {
    	targetting = TargetMode.Normal;
    }
    
    public static void stopAllVision() {
    	DriveBase.disablePID();
    	DriveBase.setPIDNormal();
    	Vision.useAngleFromNavx();
    	Vision.setNormalTargetMode();
    }
    
    public static void periodic() {
    	if(targetting == TargetMode.PegDetector) {
    		SmartDashboard.putBoolean("ProcessingGear", false);
    		processPegFrame();
    	} else if(targetting == TargetMode.GearDetector) {
    		SmartDashboard.putBoolean("ProcessingGear", true);
    		processGearFrame();
    	}
    	
    	SmartDashboard.putString("TargettingMode", targetting.toString());
    }
	
}
