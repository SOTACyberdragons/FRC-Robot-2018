/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class TurnRadiusPastAngle extends Command {
	
	private double targetAngleDeg;
	private double turnSpeed;
	private double turnRadiusIn;
	private int turnDirection;

	/**
	 * No PID
	 * @param radius
	 * @param angle
	 * @param speed
	 * @param left
	 */
	public TurnRadiusPastAngle(double radius, double angle, double speed) {
		requires(Robot.drivetrain);
		
		this.targetAngleDeg = angle;
		this.turnSpeed = speed;
		this.turnRadiusIn = radius;
	}

	@Override
	protected void initialize() {
		
		//logs
		System.out.println();
		System.out.println("TurnRadiusPastAngle Initiated");
		System.out.println("  Turn Radius: " + turnRadiusIn);
	    	System.out.println("  Turn Angle: " + targetAngleDeg + " Degrees");
	    	System.out.println("  Drive Speed: " + turnSpeed);
	    	System.out.println("  Turn Direction (1 left, -1 right: " + turnDirection);
    	
		Robot.drivetrain.resetSensors();
	}

	@Override
	protected void execute() {
		Robot.drivetrain.safeArcadeDrive(turnSpeed, turnDirection * Math.exp(-turnRadiusIn / Robot.drivetrain.WHEEL_BASE_WIDTH_IN));
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.drivetrain.getHeading()) >= targetAngleDeg;
	}

	@Override
	protected void end() {
		Robot.drivetrain.resetSensors();
		System.out.println("Turn Radius To Angle Command Complete");
	}
	
	protected void interrupted() {
		end();
	}
}
