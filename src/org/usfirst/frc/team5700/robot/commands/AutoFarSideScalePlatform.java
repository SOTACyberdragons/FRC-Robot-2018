package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.RightFarSideScalePlatform;
import org.usfirst.frc.team5700.robot.path.Waypoints.LeftFarSideScalePlatform;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoFarSideScalePlatform extends CommandGroup {

	double maxSpeed;
	
    public AutoFarSideScalePlatform(Side side) {
    	
		maxSpeed = Drivetrain.MAX_SPEED * 0.6;
		switch(side) {
			case LEFT:
				//left starting position, scale will be on the right
				addSequential(new FollowPath(new LeftFarSideScalePlatform(), maxSpeed));
				addSequential(new MoveElevatorDistance(58), 1.5);
				addSequential(new MoveArmAndElevatorDistance(58, 270), 0.5);
			case RIGHT: 
				//right starting position, scale will be on the left
				addSequential(new FollowPath(new RightFarSideScalePlatform(), maxSpeed));
				addSequential(new MoveElevatorDistance(58), 1.5);
				addSequential(new MoveArmAndElevatorDistance(58, 90), 0.5);
		}
		
    	
    }

}

