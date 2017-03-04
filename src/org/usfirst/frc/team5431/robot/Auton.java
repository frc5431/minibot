package org.usfirst.frc.team5431.robot;
import org.usfirst.frc.team5431.robot.DriveBase;
import edu.wpi.first.wpilibj.internal.HardwareTimer;

class Timing {
	
	public static HardwareTimer h_timer = new HardwareTimer();
	
	public static double start_time = 0.0;
	
	public static double getSeconds() {
		return h_timer.getFPGATimestamp();
	}
	
	public static boolean waited(double seconds){
		if (start_time == 0){
			start_time = getSeconds();
		}
		double current_time = getSeconds();
		
		if(current_time - start_time >= seconds){
			start_time = 0.0;
			return true;
		}
		return false;
	}
	
}

class Movement {
	public static boolean travelled(double distance){
		
	}
	
	public static void driveForward(double power){
		
	}
	
	public static void driveBackward(double power){
		
	}

	public static void turnLeft(double power){
		
	}
	
	public static void turnRight(double power){
		
	}
	
	public static void stayStill(){
		
	}



}


public class Auton {

	public static int main_state;
	
	public static void init() {
		main_state = 0;
	}
	
	public static void run() {
		
	}
	
}