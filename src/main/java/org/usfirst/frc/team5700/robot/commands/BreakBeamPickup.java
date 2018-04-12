package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BreakBeamPickup extends Command {

	boolean startedPickup = false;
	
    public BreakBeamPickup() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		startedPickup = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		if (!startedPickup && Robot.intake.getBackBreakBeam()) {
    			startedPickup = true;
    			new PickupCube().start();
    		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    		startedPickup = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
