/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team5700.robot.commands;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.CsvReader;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drive the given distance straight (negative values go backwards). Uses a
 * local PID controller to run a simple PID loop that is only enabled while this
 * command is running. The input is the averaged values of the left and right
 * encoders.
 */
public class DriveReplayMirror extends Command {
	private CsvReader csvReader;
	private Iterator<double[]> valuesIterator;
	private Timer timer = new Timer();
	private boolean timerStarted;
	private double timeOffset;
	private double replayKProp;
	private double leftDistanceOffset;
	private double rightDistanceOffset;
	private double leftError;
	private double rightError;
	private double leftErrorChange;
	private double rightErrorChange;
	private double lastLeftError = 0;
	private double lastRightError = 0;
	private double replayKDiff;
	private double headingOffset;
	private double headingError;
	private double replayKHeadingProp;
	private double lastHeadingError;
	private double headingErrorChange;
	private double replayKHeadingDiff;
	private String replayName;
	private double[] line;
	private Boolean mirror = false;

	public DriveReplayMirror(String replayName) {
		requires(Robot.drivetrain);
		this.replayName = replayName;
	}
	
	public DriveReplayMirror(String replayName, Boolean mirror) {
		this(replayName);
		this.mirror = true;
	}

	@Override
	protected void initialize() {

		timer.reset();
		timerStarted = false;
		Robot.drivetrain.resetSensors();
		
		//parameters
		replayKProp = Robot.prefs.getDouble("replayKProp", -0.1);
		Robot.prefs.putDouble("replayKProp", replayKProp);
		replayKDiff = Robot.prefs.getDouble("replayKDiff", 0.04);
		Robot.prefs.putDouble("replayKDiff", replayKDiff);
		replayKHeadingProp = Robot.prefs.getDouble("replayKHeadingProp", 0);
		Robot.prefs.putDouble("replayKHeadingProp", replayKHeadingProp);
		replayKHeadingDiff = Robot.prefs.getDouble("replayKHeadingDiff", 0);
		Robot.prefs.putDouble("replayKHeadingDiff", replayKHeadingDiff);

		try {
			csvReader = new CsvReader(replayName);
			valuesIterator = csvReader.getValues().iterator();
			if (valuesIterator.hasNext()) {
				line = valuesIterator.next();
				while (line[1] == 0.0 && valuesIterator.hasNext()) {
					line = valuesIterator.next();
				}
			}

			//System.out.println("move_value: " + nextLine[1] + ", rotate_value: " + nextLine[2]);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Init completed");
	}

	//	Robot.csvLogger.writeData(
	//			timer.get(), 
	//			moveValue, //move input
	//			rotateValue, //rotate input
	//			leftMotorSpeed,
	//			rightMotorSpeed,
	//			getAverageEncoderRate(),
	//			leftEncoder.getRate(),
	//			rightEncoder.getRate(),
	//			leftEncoder.getDistance(),
	//			rightEncoder.getDistance(),
	//			gyro.getAngle()
	//			);

	@Override
	protected void execute() {
		
		//switch left and right encoder sides and heading if mirrored
		double leftEncoderDistance = mirror ? Robot.drivetrain.getRightEncoder().getDistance() : 
			Robot.drivetrain.getLeftEncoder().getDistance();
		double rightEncoderDistance = mirror ? Robot.drivetrain.getLeftEncoder().getDistance() : 
			Robot.drivetrain.getRightEncoder().getDistance();
		
		double heading = Robot.drivetrain.getHeading();
		heading = mirror ? -heading : heading;

		if (!timerStarted) {
			timer.start();
			timeOffset = line[0] - timer.get();
			leftDistanceOffset = line[8] - leftEncoderDistance;
			rightDistanceOffset = line[9] - rightEncoderDistance;
			headingOffset = line[10] - heading;
			System.out.println("Offset: " + timeOffset);
			timerStarted = true;
		}
		
		double time = timer.get();
		double periodic_offset = line[0] - time - timeOffset;
		System.out.println("In execute, time difference: " + periodic_offset);
		System.out.println("time in file: " + line[0] + ", timer: "  + time + ", timeOffset: " + timeOffset);
		SmartDashboard.putNumber("Time difference: ", periodic_offset);
		
		leftError = leftEncoderDistance - line[8] + leftDistanceOffset;
		rightError = rightEncoderDistance - line[9] + rightDistanceOffset;
		headingError = heading - line[10] + headingOffset;
		leftErrorChange = lastLeftError - leftError;
		rightErrorChange = lastRightError - rightError;
		headingErrorChange = lastHeadingError - headingError;
		lastLeftError = leftError;
		lastRightError = rightError;
		lastHeadingError = headingError;

		double leftMotorSpeed = line[3] + replayKProp * leftError - replayKDiff * leftErrorChange
				- replayKHeadingProp * headingError - replayKHeadingDiff * headingErrorChange;
		double rightMotorSpeed = line[4] + replayKProp * rightError - replayKDiff * rightErrorChange
				+ replayKHeadingProp * headingError + replayKHeadingDiff * headingErrorChange;

		SmartDashboard.putNumber("leftError", leftError);
		SmartDashboard.putNumber("rightError", rightError);
		SmartDashboard.putNumber("headingError", headingError);

		System.out.println("Left motor output: " + leftMotorSpeed + ", right motor output: " + rightMotorSpeed);


		Timer.delay(Math.max(periodic_offset, 0));
		
		//swap motor outputs if mirrored

		Robot.drivetrain.drive.tankDrive(mirror ? rightMotorSpeed : leftMotorSpeed, 
				mirror ? leftMotorSpeed : rightMotorSpeed, false); //disable squared
		if (valuesIterator.hasNext()) {
			line = valuesIterator.next();

		}
	}

	@Override
	protected boolean isFinished() {
		return !valuesIterator.hasNext();
	}

	@Override
	protected void end() {
		System.out.println("Replay ended");
		Robot.drivetrain.safeArcadeDrive(0, 0);
		timer.stop();
		timer.reset();
	}

}
