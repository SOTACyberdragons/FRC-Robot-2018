package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {

	private SpeedController leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
	private SpeedController rightMotor = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);		

	private RobotDrive drive = new RobotDrive(leftMotor, rightMotor);



	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	

	/**
	 * Arcade Drive
	 * @param rightStick joystick is for moving forwards and backwards
	 * @param leftStick joystick is for turning
	 */
	public void arcadeDrive(Joystick leftStick, Joystick rightStick, boolean squaredInputs) {
		double speed = Robot.oi.driveSlow() ? 0.6 : 1.0;
		double direction = Robot.oi.directionToggle() ? -1 : 1;
		drive.arcadeDrive(-rightStick.getY(), -leftStick.getX(), squaredInputs);
	}

	public void drive(double outputMagnitude, double curve) {
		drive.drive(outputMagnitude, curve);
	}

	public void stop() {
		drive.drive(0.0, 0.0);
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}
}


