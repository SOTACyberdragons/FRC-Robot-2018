package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5700.robot.path.Waypoints.RightFarSideScalePlatform;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoFarSideScalePlatform extends CommandGroup {

	double maxSpeed;
	
    public AutoFarSideScalePlatform() {
    	
		maxSpeed = Drivetrain.MAX_SPEED * 0.6;
    	System.out.println("MAX SPEED: " + maxSpeed);
    	System.out.println("IN COMMANDGROUP!!!!");
		addSequential(new FollowPath(new RightFarSideScalePlatform(), maxSpeed));
		System.out.println("ENDING COMMANDGROUP!!!!");
		
		addSequential(new MoveElevatorDistance(52), 1.5);
		addSequential(new MoveArmAndElevatorDistance(50, 270), 0.5);
		
    }

}

