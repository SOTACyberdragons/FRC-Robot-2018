package org.usfirst.frc.team5700.robot.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class CenterToLeftSwitch {
	
	public static Waypoint[] points() {
		Waypoint[] points = new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(65, 40, Pathfinder.d2r(30)),	// Convert radians to degrees: Pathfinder.d2r(45)
				new Waypoint(97, 60, Pathfinder.d2r(81))
		};
		return points;
	}

}
