package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoLeftSideSwitch extends CommandGroup {

    public AutoLeftSideSwitch() {
		addSequential(new DriveReplay("SideSwitch"));
		addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
		addSequential(new ReleaseCube());
    }
}
