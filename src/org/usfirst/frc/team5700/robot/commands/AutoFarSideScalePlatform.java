package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.RightFarSideScalePlatform;
import org.usfirst.frc.team5700.robot.path.Waypoints.LeftFarSideScalePlatform;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoFarSideScalePlatform extends CommandGroup {

	double maxSpeed;

	public AutoFarSideScalePlatform(Side scaleSide) {

		maxSpeed = Drivetrain.MAX_SPEED * 0.6;
		switch(scaleSide) {
		case RIGHT:
			//left starting position, scale will be on the right
			addSequential(new FollowPath(new LeftFarSideScalePlatform(), maxSpeed));
			addSequential(new MoveArmToAngle(180), 0.5);
			addSequential(new MoveElevatorDistance(58), 2);
			addSequential(new MoveArmAndElevatorDistance(58, 90), 1);
			addSequential(new ReleaseCube(), 0.5);
			//move to cruise 
			addSequential(new MoveArmAndElevatorDistance(2, 180, 0.5, 0));
			break;
		case LEFT: 
			//right starting position, scale will be on the left
			addSequential(new FollowPath(new RightFarSideScalePlatform(), maxSpeed));
			addSequential(new MoveArmToAngle(180), 0.5);
			addSequential(new MoveElevatorDistance(58), 2);
			addSequential(new MoveArmAndElevatorDistance(58, 270), 1);
			addSequential(new ReleaseCube(), 0.5);
			//move to cruise 
			addSequential(new MoveArmAndElevatorDistance(2, 180, 0.5, 0));
			break;
		case UNKNOWN:
			//decide what to do if field data is not clear
			break;
		default:
			break;
		}


	}

}

