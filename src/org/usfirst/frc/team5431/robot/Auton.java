package org.usfirst.frc.team5431.robot;
import org.usfirst.frc.team5431.robot.DriveBase;
import edu.wpi.first.wpilibj.internal.HardwareTimer;

class Timing {
	
	public HardwareTimer h_timer = new HardwareTimer();
	
	public double start_time = 0.0;
	
	public double getSeconds() {
		return h_timer.getFPGATimestamp();
	}
	
	public boolean waited(double seconds){
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
	public boolean travelled(double distance){
		
	}
	
	public void driveForward(double power){
		DriveBase.drive(-power, -power);
	}
	
	public void driveBackward(double power){
		
	}

	public void turnLeft(double power){
		
	}
	
	public void turnRight(double power){
		
	}
	
	public void stayStill(){
		
	}



}


public class Auton {

	public int main_state;
	
	public void init() {
		main_state = 0;
	}
	
	public void run() {
		
	}
	
}