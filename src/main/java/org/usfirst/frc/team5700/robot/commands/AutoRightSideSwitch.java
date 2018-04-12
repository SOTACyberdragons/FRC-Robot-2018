package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightSideSwitch extends CommandGroup {

	public AutoRightSideSwitch() {
		addSequential(new DriveReplay("SideSwitch"));
		addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
		addSequential(new ReleaseCube());
	}
}
