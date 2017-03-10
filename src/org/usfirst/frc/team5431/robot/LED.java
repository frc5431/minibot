package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LED {
	private static SerialPort serial;
	private static int gearTimer = 0;
	private static boolean previousGearState = false;
	private static boolean previousTimeState = false;
	
	public static void init(){
		try{
			serial = new SerialPort(115200, Port.kUSB1, 8, Parity.kNone, StopBits.kOne);
		}catch(Exception e){
			//do nothing
			System.err.println(e.getMessage());
		}
	}
	
	public static void setGear(boolean value){
		try{
			gearTimer--;
			if(value != previousGearState){
				if(value){
					gearTimer = 100;
					serial.writeString("g");
					previousGearState = value;
				}else if(gearTimer <= 0){
					serial.writeString("o");
					previousGearState = value;
				}
				
			}
		}catch(Exception e){
			//do nothing
			System.err.println(e.getMessage());
		} 
	}
	
	public static void setTimeElapsed(double d){
		try{
			boolean value = d > 120;
			if(value != previousTimeState){
				if(value){
					serial.writeString("c");
				}else{
					serial.writeString("s");
				}
				
				previousTimeState = value;
			}
		}catch(Exception e){
			//do nothing
			System.err.println(e.getMessage());
		}
	}
}
