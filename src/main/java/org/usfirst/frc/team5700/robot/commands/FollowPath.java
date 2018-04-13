package org.usfirst.frc.team5700.robot.commands;

import java.io.File;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {

	public static final double kP = 0.0;
	public static final double kI = 0;
	public static final double kD = 0;
	public static final double kF = 1 / Drivetrain.kMaxSpeed;
	public static final double kA = 0;
	public static final double kAngleP = 0; //0.8 * (-1.0/80.0);
	private Trajectory trajectory;
	private TankModifier modifier;
	private DistanceFollower left;
	private DistanceFollower right;
	private Drivetrain drive;


	public FollowPath() {

		Waypoint[] points = new Waypoint[] {
				new Waypoint(-80, -20, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
				new Waypoint(-40, -40, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
				new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 
				Trajectory.Config.SAMPLES_HIGH, 
				0.05, 
				Drivetrain.kMaxSpeed / 2, 
				Drivetrain.kMaxAccel, 
				Drivetrain.kMaxJerk);

		trajectory = Pathfinder.generate(points, config);
		modifier = new TankModifier(trajectory).modify(Drivetrain.kWheelBaseWidth);

		left = new DistanceFollower(modifier.getLeftTrajectory());
		right = new DistanceFollower(modifier.getRightTrajectory());

		// The first argument is the proportional gain. Usually this will be quite high
		// The second argument is the integral gain. This is unused for motion profiling
		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the 
		// trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
		left.configurePIDVA(kP, kI, kD, kF, kA);
		right.configurePIDVA(kP, kI, kD, kF, kA);

		drive = Robot.drivetrain;
//		File myFile = new File("/home/lvuser/myfile.csv");
//		Pathfinder.writeToCSV(myFile, trajectory);

	}

	protected void initialize() {
		drive.resetSensors();
		left.reset();
		right.reset();
	}

	protected void execute() {
		double leftMotorOutput = left.calculate(drive.getLeftEncoder().getDistance());
		double rightMotorOutput = right.calculate(drive.getRightEncoder().getDistance());

		double gyro_heading = drive.getHeading();    // Assuming the gyro is giving a value in degrees
		double desired_heading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees

		double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
		double angleCorrection = kAngleP * angleDifference;

		drive.boostedTankDrive(leftMotorOutput + angleCorrection, rightMotorOutput - angleCorrection);
	}

	protected boolean isFinished() {
		return left.isFinished() && right.isFinished();
	}

	protected void end() {

	}

	protected void interrupted() {
		end();
	}
}
