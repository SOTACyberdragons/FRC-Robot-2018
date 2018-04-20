package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.path.Waypoints.*;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCrossBaseline extends CommandGroup {

	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
		
    public AutoCrossBaseline() {
//		addSequential(new DriveReplay("SideSwitch"));
		addSequential(new FollowPath(new CrossBaseline(), maxSpeed));
    }
}
