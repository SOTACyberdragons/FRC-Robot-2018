package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.CenterToLeftSwitch;
import org.usfirst.frc.team5700.robot.path.Waypoints.CenterToRightSwitch;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCenterSwitch extends CommandGroup {
	
    public AutoCenterSwitch(Side side) {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	
    	switch (side) {
    		case LEFT:
    			addSequential(new FollowPath(new CenterToLeftSwitch(), maxSpeed));
    			addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
    			break;
    		case RIGHT:
    			addSequential(new FollowPath(new CenterToRightSwitch(), maxSpeed));
    			addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
    			break;
    	}
    	
		addSequential(new ReleaseCube());
    }
}
