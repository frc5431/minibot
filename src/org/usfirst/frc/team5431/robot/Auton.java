package org.usfirst.frc.team5431.robot;
import edu.wpi.first.wpilibj.Timer;

/*
class Auton{
	int state = 0;
	void run(int selection)
	{
		switch(selection)
		{
		case 0:
			//Do Nothing
			break;
		case 1:
			DriveForward(state);
			break;
		case 2:
			RedLeft(state);
			break;
		default:
			break;
		}
	}
	
	void DriveForward(int state)
	{
		switch(state)
		{
		case 0:
			DriveBase.resetEncoders();
			state = 1;
		case 1:
			DriveBase.drive(0.5, 0.5);
			if(DriveBase.leftEncoder() >= 113 || DriveBase.rightEncoder() >= 113)
			{
				DriveBase.drive(0.0, 0.0);
				state = 2;
			}
			break;
		case 2:
			DriveBase.drive(0.0, 0.0);
			state = 3;
			break;
		case 3:
			break;
		}
	}
	
	void RedLeft(int state)
	{
		switch(state)
		{
		case 0:
			DriveBase.resetEncoders();
			state = 1;
		case 1:
			DriveBase.drive(0.5, 0.5);
			if(DriveBase.leftEncoder() >= 113 || DriveBase.rightEncoder() >= 113)
			{
				DriveBase.drive(0.0, 0.0);
				state = 2;
			}
			break;
		case 2:
			Timer.delay(2);//Delays everything - NOT GOOD PRACTICE
			state = 3;
			break;
		case 3:
			DriveBase.drive(-0.3, 0.3);
			if(DriveBase.getYaw() >= 54)
			{
				DriveBase.drive(0.0, 0.0);
				state = 4;
			}
			break;
		case 4:
			DriveBase.resetEncoders();
			state = 5;
			break;
		case 5:
			DriveBase.drive(0.5, 0.5);
			if(DriveBase.leftEncoder() >= 84 || DriveBase.rightEncoder() >= 84)
			{
				DriveBase.drive(0.0, 0.0);
				state = 6;
			}
			break;
		case 6:
			//Done with RedLeft
			break;
		default:
			//Um . . .
			break;
		}
	}
}*/