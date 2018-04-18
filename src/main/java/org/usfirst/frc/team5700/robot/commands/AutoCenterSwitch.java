package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.path.Waypoints.CenterToLeftSwitch;
import org.usfirst.frc.team5700.robot.path.Waypoints.CenterToRightSwitch;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCenterSwitch extends CommandGroup {
	
    public AutoCenterSwitch(Side side) {
    	double maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	
    	Command followPathToRightSwitch = new FollowPath(new CenterToRightSwitch(), maxSpeed);
		Command followPathToLeftSwitch = new FollowPath(new CenterToLeftSwitch(), maxSpeed);
    	
    	switch (side) {
    		case LEFT:
    			addSequential(followPathToLeftSwitch);
    			addSequential(new MoveArmAndElevatorDistance(1, 90), 0.5);
    			break;
    		case RIGHT:
    			addSequential(followPathToRightSwitch);
    			addSequential(new MoveArmAndElevatorDistance(1, 270), 0.5);
    			break;
    	}
    	
		addSequential(new ReleaseCube());
    }
}
