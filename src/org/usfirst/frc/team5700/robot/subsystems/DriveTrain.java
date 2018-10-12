package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;
import org.usfirst.frc.team5700.robot.commands.ArcadeDriveWithJoysticks;
import org.usfirst.frc.team5700.utils.BoostFilter;
import org.usfirst.frc.team5700.utils.SquareFilter;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {

	public static double maxSideAccel;

	private SpeedController leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
	private SpeedController rightMotor = new Spark(RobotMap.RIGHT_DRIVE_PWM);

	public RobotDrive drive = new RobotDrive(leftMotor, rightMotor);
	private BuiltInAccelerometer accel = new BuiltInAccelerometer();

	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	Timer timer = new Timer();

	//Encoder specs: S4T-360-250-S-D (usdigital.com)
	//S4T Shaft Encoder, 360 CPR, 1/4" Dia Shaft, Single-Ended, Default Torque
	//Encoder Distance Constants
	public final static double WHEEL_BASE_WIDTH_IN = 25; //TOOD find
	public final static double WHEEL_DIAMETER = 6;
	public final static double PULSE_PER_REVOLUTION = 360;

	private Encoder leftEncoder = new Encoder(RobotMap.LeftEncoderAChannel, RobotMap.LeftEncoderBChannel, false);
	private Encoder rightEncoder = new Encoder(RobotMap.RightEncoderAChannel, RobotMap.RightEncoderBChannel, true);

	Preferences prefs = Preferences.getInstance();

	//input limiting fields
	private double previousMoveValue = 0;
	private double positiveInputChangeLimit;
	private double moveBoost;
	private double rotateBoost;
	private double requestedMoveChange;
	private double limitedMoveValue;
	private double negativeInputChangeLimit;
	private boolean positiveInputLimitActive;
	private boolean negativeInputLimitActive;
	private double newRotateValue;
	private boolean rotateInputLimitActive;

	final static double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION;

	public DriveTrain() {
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

		leftMotor.setInverted(false);
		rightMotor.setInverted(false);

		resetSensors();
		timer.start();
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

		boolean useMoveInputLimit = prefs.getBoolean("useMoveInputLimit", true);
		prefs.putBoolean("useMoveInputLimit", useMoveInputLimit);

		SmartDashboard.putBoolean("useMoveInputLimit", useMoveInputLimit);
		if (useMoveInputLimit) {
			//check positive change
			positiveInputChangeLimit = prefs.getDouble("positiveInputChangeLimit", 0.025);
			prefs.putDouble("positiveInputChangeLimit", positiveInputChangeLimit);
			negativeInputChangeLimit = prefs.getDouble("negativeInputChangeLimit", 0.025);
			prefs.putDouble("negativeInputChangeLimit", negativeInputChangeLimit);

			if (requestedMoveChange > positiveInputChangeLimit) {
				positiveInputLimitActive = true;
				limitedMoveValue = previousMoveValue + positiveInputChangeLimit;

			}
			if (requestedMoveChange < - negativeInputChangeLimit) {
				negativeInputLimitActive = true;
				limitedMoveValue = previousMoveValue - negativeInputChangeLimit;
			}
		}

		//rotational accel.
		boolean useRotationInputLimit = prefs.getBoolean("useRotationInputLimit", false);
		prefs.putBoolean("useRotationInputLimit", useRotationInputLimit);

		SmartDashboard.putBoolean("useRotationInputLimit", useRotationInputLimit);
		double speed = getAverageEncoderRate();
		rotateInputLimitActive = false;
		newRotateValue = rotateValue;
		if (useRotationInputLimit) {

			double turnRadiusIn = -Math.log(rotateValue) * WHEEL_BASE_WIDTH_IN;
			maxSideAccel = prefs.getDouble("maxSideAccel", 60);
			prefs.putDouble("maxSideAccel", maxSideAccel);
			double radiusThreshhold = prefs.getDouble("radiusThreshhold", 10);
			prefs.putDouble("radiusThreshold", radiusThreshhold);
			double wantedSideAccel = Math.pow(speed, 2) / turnRadiusIn;
			//
			if (turnRadiusIn > radiusThreshhold && wantedSideAccel > maxSideAccel) {
				newRotateValue = Math.exp((-Math.pow(speed,
						2) / maxSideAccel) / WHEEL_BASE_WIDTH_IN);
				rotateInputLimitActive = true;
			}

			SmartDashboard.putBoolean("positiveInputLimitActive", positiveInputLimitActive);
			SmartDashboard.putBoolean("negativeInputLimitActive", negativeInputLimitActive);
			SmartDashboard.putNumber("accelerometer -Y", - accel.getY());
			SmartDashboard.putNumber("accelerometer X", accel.getX());
		}

		previousMoveValue = limitedMoveValue;
		boostedArcadeDrive(limitedMoveValue, newRotateValue);
	}

	public void safeArcadeDriveDelayed(double moveValue, double rotateValue) {
//		System.out.println("Timer before delay: " + timer.get());
		Timer.delay(0.01);
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

		moveBoost = prefs.getDouble("moveBoost", 0.05);
		rotateBoost = prefs.getDouble("rotateBoost", 0.05);
		BoostFilter moveBoostFilter = new BoostFilter(moveBoost);
		BoostFilter rotateBoostFilter = new BoostFilter(rotateBoost);
		arcadeDrive(moveBoostFilter.output(moveValue), rotateBoostFilter.output(rotateValue));

	}

	@SuppressWarnings("deprecation")
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

		double turnCorrection = prefs.getDouble("turnCorrection", 0);
		prefs.putDouble("turnCorrection", turnCorrection);

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

	public void arcadeDriveDelayed(double moveValue, double rotateValue) {
		arcadeDrive(moveValue, rotateValue);
	}

	public void drive(double outputMagnitude, double curve) {
		drive.drive(-outputMagnitude, -(curve));
	}

	public void stop() {
		drive.drive(0.0, 0.0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}

	public double getXAccel() {
		return accel.getX();
	}

	public double getYAccel() {
		return accel.getY();
	}

	public double getZAccel() {
		return accel.getZ();
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
	
}


