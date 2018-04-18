package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;
import org.usfirst.frc.team5700.utils.BoostFilter;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Drivetrain extends Subsystem {

	//Encoder specs: S4T-360-250-S-D (usdigital.com)
	//S4T Shaft Encoder, 360 CPR, 1/4" Dia Shaft, Single-Ended, Default Torque
	//Encoder Distance Constants
	public final static double WHEELBASE_WIDTH = 24; //TODO find
	public final static double WHEEL_DIAMETER = 6;
	public final static double PULSE_PER_REVOLUTION = 360;
	public final static double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION;
	public final static double MAX_SPEED = 110.0;
	public static final double MAX_ACCEL = 1.0 / 0.0254; //0.2g in in/s^2
	public static final double MAX_JERK = 30 / 0.0254; //from example code in Pathfinder

	//motors and drive
	private SpeedController leftMotor;
	private SpeedController rightMotor;
	private DifferentialDrive drive;
	
	//sensors
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	@SuppressWarnings("unused")
	private BuiltInAccelerometer accel;
	private ADXRS450_Gyro gyro;
	
	private Timer timer;

	private Preferences prefs;

	//input limiting fields
	private double positiveInputChangeLimit;
	private double negativeInputChangeLimit;
	private double previousMoveValue;
	private double requestedMoveChange;
	private double limitedMoveValue;

	private boolean positiveInputLimitActive;
	private boolean negativeInputLimitActive;
	
	private double moveBoost;
	private double rotateBoost;

	public Drivetrain() {

		super();
		
		leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
		rightMotor = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
		drive = new DifferentialDrive(leftMotor, rightMotor);
		
		leftEncoder = new Encoder(RobotMap.LEFT_ENCODER_A_CHANNEL, RobotMap.LEFT_ENCODER_B_CHANNEL, true);
		leftEncoder.setName("Drivetrain", "Left Encoder");
		rightEncoder = new Encoder(RobotMap.RIGHT_ENCODER_A_CHANNEL, RobotMap.RIGHT_ENCODER_B_CHANNEL, false);
		rightEncoder.setName("Drivetrain", "Right Encoder");
		accel = new BuiltInAccelerometer();
		gyro = new ADXRS450_Gyro();
		
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

		timer = new Timer();
		timer.start();
		
		prefs = Preferences.getInstance();
		resetSensors();
		
	}

	/**
	 * Limits move input changes
	 * @param moveValue input for forward/backward motion
	 * @param rotateValue input for rotation
	 * 
	 * After change limit is applied, the input is passed to boosted arcadeDrive.
	 * Input from joystick should already be filtered for sensitivity
	 */
	public void safeArcadeDrive(double moveValue, double rotateValue) {
		
		requestedMoveChange = moveValue - previousMoveValue;
		limitedMoveValue = moveValue;
		positiveInputLimitActive = false;
		negativeInputLimitActive = false;

		boolean useMoveInputLimit = prefs.getBoolean("Drivetrain/useMoveInputLimit", true);
		prefs.putBoolean("Drivetrain/useMoveInputLimit", useMoveInputLimit);

		SmartDashboard.putBoolean("Drivetrain/useMoveInputLimit", useMoveInputLimit);
		if (useMoveInputLimit) {
			//check positive change
			positiveInputChangeLimit = prefs.getDouble("Drivetrain/positiveInputChangeLimit", 0.025);
			prefs.putDouble("Drivetrain/positiveInputChangeLimit", positiveInputChangeLimit);
			negativeInputChangeLimit = prefs.getDouble("Drivetrain/negativeInputChangeLimit", 0.025);
			prefs.putDouble("Drivetrain/negativeInputChangeLimit", negativeInputChangeLimit);

			if (requestedMoveChange > positiveInputChangeLimit) {
				
				positiveInputLimitActive = true;
				limitedMoveValue = previousMoveValue + positiveInputChangeLimit;

			}
			if (requestedMoveChange < - negativeInputChangeLimit) {
				
				negativeInputLimitActive = true;
				limitedMoveValue = previousMoveValue - negativeInputChangeLimit;
				
			}
		}

		SmartDashboard.putBoolean("Drivetrain/positiveInputLimitActive", positiveInputLimitActive);
		SmartDashboard.putBoolean("Drivetrain/negativeInputLimitActive", negativeInputLimitActive);
		
		previousMoveValue = limitedMoveValue;
		boostedArcadeDrive(limitedMoveValue, rotateValue);
	}

	public void safeArcadeDriveDelayed(double moveValue, double rotateValue, double delay) {
		//		System.out.println("Timer before delay: " + timer.get());
		Timer.delay(delay);
		//		System.out.println("Timer after delay: " + timer.get());
		safeArcadeDrive(moveValue, rotateValue);
	}

	/**
	 * Applies BoostFilter to input
	 * @param moveValue input for forward/backward motion
	 * @param rotateValue input for rotation
	 * 
	 * After change limit is applied, the input is passed to boosted arcadeDrive
	 */
	public void boostedArcadeDrive(double moveValue, double rotateValue) {

		moveBoost = prefs.getDouble("Drivetrain/moveBoost", 0.05);
		rotateBoost = prefs.getDouble("Drivetrain/rotateBoost", 0.05);
		
		BoostFilter moveBoostFilter = new BoostFilter(moveBoost);
		BoostFilter rotateBoostFilter = new BoostFilter(rotateBoost);
		
		arcadeDrive(moveBoostFilter.output(moveValue), rotateBoostFilter.output(rotateValue));

	}
	
	public void boostedTankDrive(double leftMotorOutput, double rightMotorOutput) {

		//TODO these are for practice robot, change for competition
		double leftBoost = 0.20;
		double rightBoost = 0.17;
		
		BoostFilter leftBoostFilter = new BoostFilter(leftBoost);
		BoostFilter rightBoostFilter = new BoostFilter(rightBoost);
		double boostedLeftOutput = leftBoostFilter.output(leftMotorOutput);
		double boostedRightOutput = rightBoostFilter.output(rightMotorOutput);
		
		tankDrive(boostedLeftOutput, boostedRightOutput, false);
	}

	public void arcadeDrive(double moveValue, double rotateValue) {

		double leftMotorSpeed;
		double rightMotorSpeed;
		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}

		double filteredLeftMotorSpeed = leftMotorSpeed; //SquareFilter.output(leftMotorSpeed);
		double filteredRightMotorSpeed = rightMotorSpeed; //SquareFilter.output(rightMotorSpeed);

		double turnCorrection = prefs.getDouble("Drivetrain/turnCorrection", 0);
		prefs.putDouble("Drivetrain/turnCorrection", turnCorrection);

		if (filteredLeftMotorSpeed > 0 && filteredRightMotorSpeed > 0) {
			filteredLeftMotorSpeed *= 1 + turnCorrection;
			filteredRightMotorSpeed *= 1 - turnCorrection;
		}

		//always record values passed to the drive
		Robot.csvLogger.writeData(
				timer.get(), 
				moveValue, //move input
				rotateValue, //rotate input
				filteredLeftMotorSpeed,
				filteredRightMotorSpeed,
				getAverageEncoderRate(),
				leftEncoder.getRate(),
				rightEncoder.getRate(),
				leftEncoder.getDistance(),
				rightEncoder.getDistance(),
				gyro.getAngle()
				);

		drive.tankDrive(filteredLeftMotorSpeed, filteredRightMotorSpeed, false); //squared input by default
	}

	public void stop() {
		drive.tankDrive(0.0, 0.0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}

	public void resetSensors() {
		gyro.reset();
		leftEncoder.reset();
		rightEncoder.reset();
	}

	public double getDistance() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	/**
	 * @return rate, ticks per second
	 */
	public double getAverageEncoderRate() {
		return ((leftEncoder.getRate() + rightEncoder.getRate())/2);
	}

	public double getHeading() {
		return gyro.getAngle();
	}

	public Encoder getRightEncoder() {
		return rightEncoder;
	}

	public Encoder getLeftEncoder() {
		return leftEncoder;
	}

	public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs) {
		drive.tankDrive(leftSpeed, rightSpeed, squaredInputs);
	}

}

