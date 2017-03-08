package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;

public class LED {
	private static SerialPort serial;
	
	public static void init(){
		serial = new SerialPort(115200, Port.kUSB1, 8, Parity.kNone, StopBits.kOne);
	}
	
	public static void setGear(boolean value){
		if(value){
			serial.writeString("g");
		}else{
			serial.writeString("o");
		}
	}
	
	public static void setTimeElapsed(int timeElapsed){
		if(timeElapsed > 120){
			serial.writeString("c");
		}else{
			serial.writeString("s");
		}
	}
}
