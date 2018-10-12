package org.usfirst.frc.team5700.utils;

public class DriveTurnDrivePath {
		
		public double turnRadius;
		public double turnAngle;
		public double firstDistance;
		public double secondDistance;
	
		public void setTurnToAngle(double radius, double angle) {
			turnRadius = radius;
			turnAngle = angle;
		}
		
		public void setFirstDistance(double distance) {
			firstDistance = distance;
		}
		
		public void setSecondDistance(double distance) {
			firstDistance = distance;
		}
}
