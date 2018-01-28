package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.RungClimber.Direction;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RungClimbReverse extends Command {

    public RungClimbReverse() {
    		requires(Robot.rungClimber);
       
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.rungClimber.climb(0.5, Direction.DOWN);
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
