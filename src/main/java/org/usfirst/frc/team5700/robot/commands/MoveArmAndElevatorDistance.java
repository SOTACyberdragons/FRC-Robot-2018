package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MoveArmAndElevatorDistance extends CommandGroup {

    public MoveArmAndElevatorDistance(double elevatorHeightIn, double armAngleDeg) {

    	addParallel(new MoveElevatorDistance(elevatorHeightIn));
    	addSequential(new MoveArmToAngle(armAngleDeg));
    }
    
    public MoveArmAndElevatorDistance(double elevatorHeightIn, double armAngleDeg, double elevatorDelaySec, double armDelaySec) {

    	addParallel(new MoveElevatorDistance(elevatorHeightIn, elevatorDelaySec));
    	addSequential(new MoveArmToAngle(armAngleDeg, armDelaySec));
    }
}
