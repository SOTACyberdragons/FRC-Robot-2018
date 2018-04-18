package org.usfirst.frc.team5700.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//PWM
	public static final int LEFT_DRIVE_MOTOR = 0;
	public static final int RIGHT_DRIVE_MOTOR = 1;
	public static final int INTAKE_MOTORS = 2;
	public static final int CLIMBER_MOTOR = 6;

	//Pneumatics
	public static final int EXTEND_INTAKES_CHANNEL = 0;
	public static final int OPEN_GRABBER_CHANNEL = 1;
	public static final int CLOSE_GRABBER_CHANNEL = 2;
	public static final int ASSIST_HOLD_CHANNEL = 3;
	public static final int ASSIST_RELEASE_CHANNEL = 4;
	
	//practice bot
//	static final int GRABBER_CHANNEL = 4;
//	public static final int EXTEND_RIGHT_INTAKES_CHANNEL = 3; 
//	public static final int CLOSE_RIGHT_INTAKES_CHANNEL = 2;
//	public static final int EXTEND_LEFT_INTAKES_CHANNEL = 1;
//	public static final int CLOSE_LEFT_INTAKES_CHANNEL = 0;

	//Sensors (DIO)
	public static final int LEFT_ENCODER_A_CHANNEL = 4;
	public static final int LEFT_ENCODER_B_CHANNEL = 5;
	public static final int RIGHT_ENCODER_A_CHANNEL = 6;
	public static final int RIGHT_ENCODER_B_CHANNEL = 7;
	public static final int FRONT_BREAK_BEAM_CHANNEL = 8;
	public static final int BACK_BREAK_BEAM_CHANNEL = 9;

	//practice bot
//	public static final int LEFT_ENCODER_A_CHANNEL = 0;
//	public static final int LEFT_ENCODER_B_CHANNEL = 1;
//	public static final int RIGHT_ENCODER_A_CHANNEL = 3;
//	public static final int RIGHT_ENCODER_B_CHANNEL = 4;

}