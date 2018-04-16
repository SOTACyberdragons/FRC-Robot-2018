package org.usfirst.frc.team5700.robot.path;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public interface Waypoints {

	public Waypoint[] points();

	public final class CenterToLeftSwitch implements Waypoints {
		public Waypoint[] points() {
			return new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(65, 40, Pathfinder.d2r(30)),	// Convert radians to degrees: Pathfinder.d2r(45)
					new Waypoint(97, 60, Pathfinder.d2r(81))
			};
		}

	}
	public final class CenterToRightSwitch implements Waypoints {
		public Waypoint[] points() {
			return  new Waypoint[] {
					new Waypoint(0, 0, 0),
					new Waypoint(70, -26, Pathfinder.d2r(-23)),	// Convert radians to degrees: Pathfinder.d2r(45)
					new Waypoint(97, -49, Pathfinder.d2r(-81))
			};
		}
	}
}
