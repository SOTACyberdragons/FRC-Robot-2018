package org.usfirst.frc.team5700.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//PWM
	public static final int kLeftDriveMotor = 0;
	public static final int kRightDriveMotor = 1;
	public static final int INTAKE_MOTORS = 2;
	public static final int CLIMBER_MOTOR = 6;
	
	//Pneumatics
	public static final int EXTEND_INTAKES_CHANNEL = 0;
//	public static final int EXTEND_RIGHT_INTAKES_CHANNEL = 3; 
//	public static final int CLOSE_RIGHT_INTAKES_CHANNEL = 2;
//	public static final int EXTEND_LEFT_INTAKES_CHANNEL = 1;
//	public static final int CLOSE_LEFT_INTAKES_CHANNEL = 0;
	// static final int GRABBER_CHANNEL = 4; For Practice Bot
	public static final int OPEN_GRABBER_CHANNEL = 1;
	public static final int CLOSE_GRABBER_CHANNEL = 2;
	public static final int ASSIST_HOLD_CHANNEL = 3;
	public static final int ASSIST_RELEASE_CHANNEL = 4;
	
	//Sensors (DIO)0;
	//TODO make sure these are corrected
	//competition robot
//	public static final int LeftEncoderAChannel = 4;
//	public static final int LeftEncoderBChannel = 5;
//	public static final int RightEncoderAChannel = 6;
//	public static final int RightEncoderBChannel = 7;
	public static final int FrontBreakBeamChannel = 8;
	public static final int BackBreakBeamChannel = 9;
	
	//practice robot
	public static final int LeftEncoderAChannel = 0;
	public static final int LeftEncoderBChannel = 1;
	public static final int RightEncoderAChannel = 3;
	public static final int RightEncoderBChannel = 4;

}