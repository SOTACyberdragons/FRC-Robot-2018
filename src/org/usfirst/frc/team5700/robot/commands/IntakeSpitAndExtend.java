package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeSpitAndExtend extends Command {

    public IntakeSpitAndExtend() {
    	requires(Robot.boxIntake);
    }

    protected void initialize() {
    	Robot.boxIntake.extendBoth();
    }

    protected void execute() {
    	Robot.boxIntake.spitBothMotors();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.boxIntake.retractBoth();
    	Robot.boxIntake.stopMotors();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

