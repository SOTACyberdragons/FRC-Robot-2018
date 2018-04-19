package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.LeftSideSwitch;
import org.usfirst.frc.team5700.robot.path.Waypoints.RightSideSwitch;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSideSwitch extends CommandGroup {
	
    public AutoSideSwitch(Side side) {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	
    	switch (side) {
    		case LEFT:
//    			addSequential(new DriveReplay("SideSwitch"));
    			addSequential(new FollowPath(new LeftSideSwitch(), maxSpeed));
    			addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
    			break;
    		case RIGHT:
//    			addSequential(new DriveReplay("SideSwitch"));
    			addSequential(new FollowPath(new RightSideSwitch(), maxSpeed));
    			addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
    			break;
    	}
    	
		addSequential(new ReleaseCube());
    }
}
