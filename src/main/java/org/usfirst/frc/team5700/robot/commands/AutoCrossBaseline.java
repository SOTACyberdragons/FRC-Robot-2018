package org.usfirst.frc.team5700.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoCrossBaseline extends CommandGroup {

	//TODO update with path	
    public AutoCrossBaseline() {
		addSequential(new DriveReplay("SideSwitch"));
    }
}
