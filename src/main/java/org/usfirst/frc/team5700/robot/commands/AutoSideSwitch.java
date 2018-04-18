package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.*;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Waypoint;

public class AutoSideSwitch extends CommandGroup {
	
    public AutoSideSwitch(Side side) {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	
    	Command followPathToRightSwitch = new FollowPath(new RightSideSwitch(), maxSpeed);
		Command followPathToLeftSwitch = new FollowPath(new LeftSideSwitch(), maxSpeed);
    	
    	switch (side) {
    		case LEFT:
    			addSequential(new DriveReplay("SideSwitch"));
    			addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
//    			addSequential(followPathToLeftSwitch);
//    			addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
    			break;
    		case RIGHT:
    			addSequential(new DriveReplay("SideSwitch"));
    			addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
//    			addSequential(followPathToRightSwitch);
//    			addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
    			break;
    	}
    	
		addSequential(new ReleaseCube());
    }
}
