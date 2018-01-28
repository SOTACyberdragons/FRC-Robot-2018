package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.RungClimber.Direction;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RungClimb extends Command {
	
	private double speed;
	
    public RungClimb(double climbSpeed) {

    	requires(Robot.rungClimber);
    	
    	speed = climbSpeed;
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.rungClimber.climb(speed, Direction.UP);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    		Robot.rungClimber.climb(0);
    }

    protected void interrupted() {
    	end();
    }
}
