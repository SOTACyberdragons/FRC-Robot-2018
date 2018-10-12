package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.utils.DriveTurnDrivePath;

public class AutonomousPaths {

   public static final DriveTurnDrivePath leftSide = new DriveTurnDrivePath();
   public static final DriveTurnDrivePath rightSide = new DriveTurnDrivePath();
   
   public static final double distanceToCenterOfSwitchIn = 14*12 - 28;
   
   public AutonomousPaths() {
	   leftSide.setFirstDistance(34.025);
	   leftSide.setTurnToAngle(24, 58);
	   leftSide.setSecondDistance(80.45);
			
	   rightSide.setFirstDistance(40.9);
	   rightSide.setTurnToAngle(24, -58);
	   rightSide.setSecondDistance(67.5);
   }
}
