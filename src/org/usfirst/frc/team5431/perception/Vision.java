package org.usfirst.frc.team5431.perception;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team5431.robot.DriveBase;
import org.usfirst.frc.team5431.robot.DriveBasePIDSource;

import com.kylecorry.frc.vision.CameraSource;
import com.kylecorry.frc.vision.CameraSpecs;
import com.kylecorry.frc.vision.TargetGroup;
import com.kylecorry.frc.vision.TargetGroupDetector;
import com.kylecorry.geometry.Point;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	public static UsbCamera targetCamera;
	public static CvSource processedStream;
	public static CameraSource camera;
	public static boolean initialized = false, isRunning = false;
	public static TargetMode targetMode;
	public static TargetGroupDetector detector;
	public static int imageWidth = 640, imageHeight = 480;
	public static double cX, cY, distance, angle;
	public static double viewAngle = CameraSpecs.MicrosoftLifeCam.HORIZONTAL_VIEW_ANGLE;
	public static final PegSpecs pegSpecs = new PegSpecs();
	public static final PegGroupSpecs pegGroupSpecs = new PegGroupSpecs();
	
	public static enum TargetMode {
		Normal, PegDetector
	}
	
	public static void setResolution(int x, int y) {
		imageWidth = x;
		imageHeight = y;
	}
	
	public static int[] getResolution() {
		return new int[] {imageWidth, imageHeight};
	}
	
	public static double getTargetAngle() {
		return angle;
	}
	
	public static void init() {
		if(initialized) return;
		targetCamera = new UsbCamera("cam", 0); //CameraServer.getInstance().startAutomaticCapture(0);//.putVideo("PegTartet", 640, 480);
		processedStream = CameraServer.getInstance().putVideo("PegProcessed", 640, 480);
		setNormalMode();
		camera = new CameraSource(targetCamera);
		camera.setResolution(imageWidth, 480);
		initialized = true;
		camera.start();
	}
	
	public static void setNormalMode() {
		targetCamera.setExposureAuto();
		targetCamera.setWhiteBalanceAuto();
		targetCamera.setBrightness(50);
	}
	
	public static void setTargetingMode() {
		targetCamera.setExposureManual(0);
		targetCamera.setBrightness(0);
		targetCamera.setWhiteBalanceManual(10000);
	}
	
	public static TargetGroupDetector createPegDetector() {
		TargetGroupDetector pegGroupDetector = new TargetGroupDetector(pegSpecs, pegGroupSpecs, (targets) -> {
			SmartDashboard.putNumber("Targets Found", targets.size());
			if(!targets.isEmpty()) {
				TargetGroup pegTape = targets.get(0); 
				Point topLeftPoint = pegTape.getPosition();
				Point centerPoint = pegTape.getCenterPosition();
				cX = centerPoint.x;
				cY = centerPoint.y;
				distance = pegTape.computeDistance(imageWidth, pegGroupSpecs.getGroupWidth(), viewAngle);
				angle = 90 - pegTape.computeAngle(imageWidth, viewAngle);
				
				Mat frame = camera.getPicture();
				Rect boundary = new Rect((int) Math.round(topLeftPoint.x),
						(int) Math.round(topLeftPoint.y),
						(int) Math.round(pegTape.getWidth()),
						(int) Math.round(pegTape.getHeight()));
				Imgproc.rectangle(frame, boundary.tl(), boundary.br(), new Scalar(0, 255, 0));	
				
				SmartDashboard.putNumber("TargetX", cX);
				SmartDashboard.putNumber("TargetY", cY);
				SmartDashboard.putNumber("TargetDistance", distance);
				SmartDashboard.putNumber("TargetAngle", angle);
				processedStream.putFrame(frame);
			}
		});
		return pegGroupDetector;
	}
	
	public static void startPIDDrive() {
		startPIDDrive(0.25);
	}
	
	public static void startPIDDrive(double drive) {
		DriveBasePIDSource.inputType = DriveBasePIDSource.InputType.Vision;
		DriveBase.drivePIDForward(drive);
	}
	
	public static void stopPIDDrive() {
		DriveBasePIDSource.inputType = DriveBasePIDSource.inputType.Navx;
		DriveBase.disablePID();
	}
	
	public static void setTargetMode(TargetMode mode) {
		targetMode = mode;
		if(targetMode == TargetMode.Normal) {
			setNormalMode();
			isRunning = false;
		} else if(targetMode == TargetMode.PegDetector) {
			setTargetingMode();
			detector = createPegDetector();
			camera.setDetector(detector);
			isRunning = true;
		}
	}
	
	public static void stop() {
		if(camera != null && isRunning) camera.stop();
		if(targetCamera != null && isRunning) targetCamera.free();
		initialized = false;
		isRunning = false;
	}
}
