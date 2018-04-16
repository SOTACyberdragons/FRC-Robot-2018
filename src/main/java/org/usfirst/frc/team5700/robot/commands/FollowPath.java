package org.usfirst.frc.team5700.robot.commands;

import java.io.File;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.path.Waypoints;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {

	private static final String dir = "/home/lvuser/pathfinder/";
	private static double kP;
	private static double kI;
	private static double kD;
	private static double kF = 1 / Drivetrain.kMaxSpeed;
	private static double kA = 0;
	private static double kAngleP;
	private static double kAngleD;
	
	private Trajectory trajectory;
	private TankModifier modifier;
	private DistanceFollower left;
	private DistanceFollower right;
	private Drivetrain drive;
	private Preferences prefs;
	private Timer timer;
	private double angleError;
	private double lastAngleError;

	public FollowPath(Waypoints waypoints, double maxSpeed) {

		timer = new Timer();
		prefs = Robot.prefs;
		drive = Robot.drivetrain;

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 
				Trajectory.Config.SAMPLES_HIGH, 
				0.02, 
				maxSpeed, 
				Drivetrain.kMaxAccel, 
				Drivetrain.kMaxJerk);

		trajectory = Pathfinder.generate(waypoints.points(), config);
		modifier = new TankModifier(trajectory).modify(Drivetrain.kWheelBaseWidth);

		left = new DistanceFollower(modifier.getLeftTrajectory());
		right = new DistanceFollower(modifier.getRightTrajectory());

		File trajectoryCsv = new File(dir + waypoints.getClass().getSimpleName() + ".csv");
		Pathfinder.writeToCSV(trajectoryCsv, trajectory);

	}

	protected void initialize() {
		drive.resetSensors();
		timer.reset();
		timer.start();
		kP = prefs.getDouble("Pathfinder/kP", 0.45);
		prefs.putDouble("Pathfinder/kP", kP);
		kD = prefs.getDouble("Pathfinder/kD", 0.01);
		prefs.putDouble("Pathfinder/kD", kD);
		kAngleP = prefs.getDouble("Pathfinder/kAngleP", 0.05);
		prefs.putDouble("Pathfinder/kAngleP", kAngleP);
		kAngleD = prefs.getDouble("Pathfinder/kAngleD", 0.0);
		prefs.putDouble("Pathfinder/kAngleD", kAngleD);

		// The first argument is the proportional gain. Usually this will be quite high
		// The second argument is the integral gain. This is unused for motion profiling
		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the 
		// trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
		left.configurePIDVA(kP, kI, kD, kF, kA);
		right.configurePIDVA(kP, kI, kD, kF, kA);

		drive.resetSensors();
		left.reset();
		right.reset();
	}

	protected void execute() {
		double leftMotorOutput = left.calculate(drive.getLeftEncoder().getDistance());
		double rightMotorOutput = right.calculate(drive.getRightEncoder().getDistance());

		double gyroHeading = - drive.getHeading();    // gyro is clockwise, pathfinder counter-clockwise
		double desiredHeading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees
		SmartDashboard.putNumber("Pathfinder/desiredHeading", desiredHeading);

		angleError = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
		double angleErrorChange = lastAngleError - angleError;
		lastAngleError = angleError;
		SmartDashboard.putNumber("Pathfinder/angleError", angleError);
		SmartDashboard.putNumber("Pathfinder/angleErrorChange", angleErrorChange);

		//		System.out.println("Pathfinder at " + timer.get() + ", output: " + leftMotorOutput);
		drive.boostedTankDrive(leftMotorOutput - (kAngleP * angleError - kAngleD * angleErrorChange), 
				rightMotorOutput + (kAngleP * angleError - kAngleD * angleErrorChange));
	}

	protected boolean isFinished() {
		return left.isFinished() && right.isFinished();
	}

	protected void end() {
		timer.stop();
	}

	protected void interrupted() {
		end();
	}
}
