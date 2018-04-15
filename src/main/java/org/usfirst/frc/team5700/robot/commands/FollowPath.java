package org.usfirst.frc.team5700.robot.commands;

import java.io.File;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class FollowPath extends Command {

	public static double kP = 0.15;
	public static double kI = 0.002;
	public static double kD = 0;
	public static double kF = 1 / Drivetrain.kMaxSpeed;
	public static double kA = 0;
	public static double kAngleP = 0; //0.8 * (-1.0/80.0);
	private Trajectory trajectory;
	private TankModifier modifier;
	private DistanceFollower left;
	private DistanceFollower right;
	private Drivetrain drive;
	private Preferences m_prefs;
	private Timer m_timer;


	public FollowPath() {
		
		m_timer = new Timer();
		m_prefs = Robot.prefs;
		Waypoint[] points = new Waypoint[] {
				new Waypoint(0, 0, 0),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees Pathfinder.d2r(-45)
				new Waypoint(96, 0, Pathfinder.d2r(-45)),                     // Waypoint @ x=-2, y=-2, exit angle=0 radians
				new Waypoint(120, -48, Pathfinder.d2r(-90))                           // Waypoint @ x=0, y=0,   exit angle=0 radians
		};

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, 
				Trajectory.Config.SAMPLES_HIGH, 
				0.02, 
				Drivetrain.kMaxSpeed, 
				Drivetrain.kMaxAccel, 
				Drivetrain.kMaxJerk);

		trajectory = Pathfinder.generate(points, config);
		modifier = new TankModifier(trajectory).modify(Drivetrain.kWheelBaseWidth);

		left = new DistanceFollower(modifier.getLeftTrajectory());
		right = new DistanceFollower(modifier.getRightTrajectory());

		drive = Robot.drivetrain;
		File myFile = new File("/home/lvuser/pathfinder/90via45.csv");
		Pathfinder.writeToCSV(myFile, trajectory);

	}

	protected void initialize() {
		drive.resetSensors();
		m_timer.reset();
		m_timer.start();
		kP = m_prefs.getDouble("Pathfinder/kP", 0.15);
		m_prefs.putDouble("Pathfinder/kP", kP);
		kD = m_prefs.getDouble("Pathfinder/kD", 0.002);
		m_prefs.putDouble("Pathfinder/kD", kD);
		kAngleP = m_prefs.getDouble("Pathfinder/kAngleP", 0.001);
		m_prefs.putDouble("Pathfinder/kAngleP", kAngleP);
		
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

		double gyro_heading = - drive.getHeading();    // gyro is clockwise, pathfinder counter-clockwise
		double desiredHeading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees
		SmartDashboard.putNumber("Pathfinder/desiredHeading", desiredHeading);

		double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyro_heading);
		double angleCorrection = kAngleP * angleDifference;
		SmartDashboard.putNumber("Pathfinder/angleCorrection", angleDifference);

//		System.out.println("Pathfinder at " + m_timer.get() + ", output: " + leftMotorOutput);
		drive.boostedTankDrive(leftMotorOutput - angleCorrection, rightMotorOutput + angleCorrection);
	}

	protected boolean isFinished() {
		return left.isFinished() && right.isFinished();
	}

	protected void end() {
		m_timer.stop();
	}

	protected void interrupted() {
		end();
	}
}
