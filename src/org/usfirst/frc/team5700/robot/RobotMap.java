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
	public static final int RIGHT_INTAKE_MOTOR = 2;
	public static final int LEFT_INTAKE_MOTOR = 3;
	//pneumatics
	public static final int GRABBER_FORWARD_CHANNEL = 0;
	public static final int GRABBER_BACKWARD_CHANNEL = 0;
}
