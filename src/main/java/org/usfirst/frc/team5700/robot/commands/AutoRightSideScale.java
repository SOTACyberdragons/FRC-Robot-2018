package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRightSideScale extends CommandGroup {

    public AutoRightSideScale() {
    		addSequential(new DriveReplayMirror("LeftSideScale", true));
    		addSequential(new MoveElevatorDistance(58), 1.5);
    		addSequential(new MoveArmAndElevatorDistance(58, 270), 0.5);
		addSequential(new ReleaseCube());
    }
}