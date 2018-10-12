package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCenterToLeftSwitch extends CommandGroup {
	
	public AutoCenterToLeftSwitch() {
		addSequential(new DriveReplay("CenterToLeftSwitch"));
		addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
		addSequential(new ReleaseCube());
    }
}
