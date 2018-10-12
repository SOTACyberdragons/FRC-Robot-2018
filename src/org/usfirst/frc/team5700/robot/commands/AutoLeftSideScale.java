package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoLeftSideScale extends CommandGroup {

    public AutoLeftSideScale() {
    		addSequential(new DriveReplay("LeftSideScale"));
    		addSequential(new MoveElevatorDistance(58), 1.5);
    		addSequential(new MoveArmAndElevatorDistance(58, 90), 0.5);
		addSequential(new ReleaseCube());
    }
}
