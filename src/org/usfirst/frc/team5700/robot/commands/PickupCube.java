package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PickupCube extends CommandGroup {

    public PickupCube() {
    	addSequential(new ReleaseCube());
    	addSequential(new MoveElevatorDistance(11), 0.5);
    	addParallel(new GrabCube());
    	addSequential(new MoveElevatorDistance(34));
    }
}
