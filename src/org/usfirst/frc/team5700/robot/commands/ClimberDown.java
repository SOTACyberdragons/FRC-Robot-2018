package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberDown extends Command {

	//TODO find good home for climber speed throttle

    public ClimberDown() {
    		requires(Robot.climber);
       
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.climber.down(-(Robot.oi.getAuxRightStick().getThrottle() + 1)/2.0);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.climber.stop();
    }

    protected void interrupted() {
    		end();
    }
}

