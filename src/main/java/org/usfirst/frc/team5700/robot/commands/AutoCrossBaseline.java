package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCrossBaseline extends CommandGroup {

	//double driveDistanceIn = 160;
	
    public AutoCrossBaseline() {
		addSequential(new DriveReplay("SideSwitch"));
    }
}
