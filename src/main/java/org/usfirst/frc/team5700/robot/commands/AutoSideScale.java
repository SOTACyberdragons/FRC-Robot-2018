package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.*;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSideScale extends CommandGroup {
	
    public AutoSideScale(Side side) {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	
    	Command followPathToRightScale = new FollowPath(new RightSideScale(), maxSpeed);
		Command followPathToLeftScale = new FollowPath(new LeftSideScale(), maxSpeed);
    	
    	switch (side) {
    		case LEFT:
    			addSequential(new DriveReplay("LeftSideScale"));
    			//addSequential(followPathToLeftScale);
        		addSequential(new MoveElevatorDistance(58), 1.5);
        		addSequential(new MoveArmAndElevatorDistance(58, 90), 0.5);
        		addSequential(new ReleaseCube());
    			break;
    			
    		case RIGHT:
    			addSequential(new AutoCrossBaseline());
    			//addSequential(followPathToRightScale);
    			//addSequential(new MoveElevatorDistance(58), 1.5);
    			//addSequential(new MoveArmAndElevatorDistance(58, 270), 0.5);
    			break;
    	}
    	
		//addSequential(new ReleaseCube());
    }
}
