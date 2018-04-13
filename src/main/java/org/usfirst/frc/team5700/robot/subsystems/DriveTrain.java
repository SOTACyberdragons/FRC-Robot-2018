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
	public final static double kWheelBaseWidth = 25; //TOOD find
	public final static double kWheelDiameter = 6;
	public final static double kPulsePerRevolution = 360;
	public final static double kDistancePerPulse = Math.PI * kWheelDiameter / kPulsePerRevolution;

	//motors and drive
	private SpeedController m_leftMotor;
	private SpeedController m_rightMotor;
	private DifferentialDrive m_drive;
	
	//sensors
	private Encoder m_leftEncoder;
	private Encoder m_rightEncoder;
	@SuppressWarnings("unused")
	private BuiltInAccelerometer m_accel;
	private ADXRS450_Gyro m_gyro;
	
	private Timer m_timer;

	private Preferences m_prefs;

	//input limiting fields
	private double m_positiveInputChangeLimit;
	private double m_negativeInputChangeLimit;
	private double m_previousMoveValue;
	private double m_requestedMoveChange;
	private double m_limitedMoveValue;

	private boolean m_positiveInputLimitActive;
	private boolean m_negativeInputLimitActive;
	
	private double m_moveBoost;
	private double m_rotateBoost;

	public Drivetrain() {

		super();
		
		m_leftMotor = new Spark(RobotMap.kLeftDriveMotor);
		m_rightMotor = new Spark(RobotMap.kRightDriveMotor);
		m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);
		
		m_leftEncoder = new Encoder(RobotMap.LeftEncoderAChannel, RobotMap.LeftEncoderBChannel, false);
		m_rightEncoder = new Encoder(RobotMap.RightEncoderAChannel, RobotMap.RightEncoderBChannel, true);
		m_accel = new BuiltInAccelerometer();
		m_gyro = new ADXRS450_Gyro();
		
		m_leftEncoder.setDistancePerPulse(kDistancePerPulse);
		m_rightEncoder.setDistancePerPulse(kDistancePerPulse);

		m_timer = new Timer();
		m_timer.start();
		
		m_prefs = Preferences.getInstance();
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
		
		m_requestedMoveChange = moveValue - m_previousMoveValue;
		m_limitedMoveValue = moveValue;
		m_positiveInputLimitActive = false;
		m_negativeInputLimitActive = false;

		boolean useMoveInputLimit = m_prefs.getBoolean("Drivetrain/useMoveInputLimit", true);
		m_prefs.putBoolean("Drivetrain/useMoveInputLimit", useMoveInputLimit);

		SmartDashboard.putBoolean("Drivetrain/useMoveInputLimit", useMoveInputLimit);
		if (useMoveInputLimit) {
			//check positive change
			m_positiveInputChangeLimit = m_prefs.getDouble("Drivetrain/positiveInputChangeLimit", 0.025);
			m_prefs.putDouble("Drivetrain/positiveInputChangeLimit", m_positiveInputChangeLimit);
			m_negativeInputChangeLimit = m_prefs.getDouble("Drivetrain/negativeInputChangeLimit", 0.025);
			m_prefs.putDouble("Drivetrain/negativeInputChangeLimit", m_negativeInputChangeLimit);

			if (m_requestedMoveChange > m_positiveInputChangeLimit) {
				
				m_positiveInputLimitActive = true;
				m_limitedMoveValue = m_previousMoveValue + m_positiveInputChangeLimit;

			}
			if (m_requestedMoveChange < - m_negativeInputChangeLimit) {
				
				m_negativeInputLimitActive = true;
				m_limitedMoveValue = m_previousMoveValue - m_negativeInputChangeLimit;
				
			}
		}

		SmartDashboard.putBoolean("Drivetrain/positiveInputLimitActive", m_positiveInputLimitActive);
		SmartDashboard.putBoolean("Drivetrain/negativeInputLimitActive", m_negativeInputLimitActive);
		
		m_previousMoveValue = m_limitedMoveValue;
		boostedArcadeDrive(m_limitedMoveValue, rotateValue);
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

		m_moveBoost = m_prefs.getDouble("Drivetrain/moveBoost", 0.05);
		m_rotateBoost = m_prefs.getDouble("Drivetrain/rotateBoost", 0.05);
		
		BoostFilter moveBoostFilter = new BoostFilter(m_moveBoost);
		BoostFilter rotateBoostFilter = new BoostFilter(m_rotateBoost);
		
		arcadeDrive(moveBoostFilter.output(moveValue), rotateBoostFilter.output(rotateValue));

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

		double turnCorrection = m_prefs.getDouble("Drivetrain/turnCorrection", 0);
		m_prefs.putDouble("Drivetrain/turnCorrection", turnCorrection);

		if (filteredLeftMotorSpeed > 0 && filteredRightMotorSpeed > 0) {
			filteredLeftMotorSpeed *= 1 + turnCorrection;
			filteredRightMotorSpeed *= 1 - turnCorrection;
		}

		//always record values passed to the drive
		Robot.csvLogger.writeData(
				m_timer.get(), 
				moveValue, //move input
				rotateValue, //rotate input
				filteredLeftMotorSpeed,
				filteredRightMotorSpeed,
				getAverageEncoderRate(),
				m_leftEncoder.getRate(),
				m_rightEncoder.getRate(),
				m_leftEncoder.getDistance(),
				m_rightEncoder.getDistance(),
				m_gyro.getAngle()
				);

		m_drive.tankDrive(filteredLeftMotorSpeed, filteredRightMotorSpeed, false); //squared input by default
	}

	public void stop() {
		m_drive.tankDrive(0.0, 0.0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoysticks());
	}

	public void resetSensors() {
		m_gyro.reset();
		m_leftEncoder.reset();
		m_rightEncoder.reset();
	}

	public double getDistance() {
		return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2;
	}

	/**
	 * @return rate, ticks per second
	 */
	public double getAverageEncoderRate() {
		return ((m_leftEncoder.getRate() + m_rightEncoder.getRate())/2);
	}

	public double getHeading() {
		return m_gyro.getAngle();
	}

	public Encoder getRightEncoder() {
		return m_rightEncoder;
	}

	public Encoder getLeftEncoder() {
		return m_leftEncoder;
	}

	public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs) {
		m_drive.tankDrive(leftSpeed, rightSpeed, squaredInputs);
	}

}


