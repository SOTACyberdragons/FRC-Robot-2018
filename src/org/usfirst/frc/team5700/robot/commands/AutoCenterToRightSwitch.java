package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCenterToRightSwitch extends CommandGroup {
	
	public AutoCenterToRightSwitch() {
		addSequential(new DriveReplay("CenterToRightSwitch"));
		addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
		addSequential(new ReleaseCube());

    }
}
