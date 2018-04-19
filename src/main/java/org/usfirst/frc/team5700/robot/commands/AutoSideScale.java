package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.LeftSideScale;
import org.usfirst.frc.team5700.robot.path.Waypoints.RightSideScale;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSideScale extends CommandGroup {

	public AutoSideScale(Side side) {
		double maxSpeed = Drivetrain.MAX_SPEED * 0.6;

		switch (side) {
			case LEFT:
				//addSequential(new DriveReplay("LeftSideScale"));
				addSequential(new FollowPath(new LeftSideScale(), maxSpeed));
				addSequential(new MoveElevatorDistance(58), 1.5);
				addSequential(new MoveArmAndElevatorDistance(58, 90), 0.5);
				break;
	
			case RIGHT:
				//addSequential(new AutoCrossBaseline());
				addSequential(new FollowPath(new RightSideScale(), maxSpeed));
				addSequential(new MoveElevatorDistance(58), 1.5);
				addSequential(new MoveArmAndElevatorDistance(58, 270), 0.5);
				break;
		}

		addSequential(new ReleaseCube());
	}
}
