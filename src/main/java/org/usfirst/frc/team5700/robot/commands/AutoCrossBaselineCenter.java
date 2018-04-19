package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.path.Waypoints.CenterToRightSwitch;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCrossBaselineCenter extends CommandGroup {
	
	/**
	 * Goes to right side of switch, doesn't drop cube
	 */
    public AutoCrossBaselineCenter() {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
		addSequential(new FollowPath(new CenterToRightSwitch(), maxSpeed));
    }
}
