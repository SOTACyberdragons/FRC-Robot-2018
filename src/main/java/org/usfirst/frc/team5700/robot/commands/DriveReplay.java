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
public class DriveReplay extends Command {
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

	public DriveReplay() {
		requires(Robot.drivetrain);
	}

	public DriveReplay(String replayName) {
		this();
		this.replayName = replayName;
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
			e.printStackTrace();
		}
		System.out.println("Init completed");
	}

	@Override
	protected void execute() {
		double leftEncoderDistance = Robot.drivetrain.getLeftEncoder().getDistance();
		double rightEncoderDistance = Robot.drivetrain.getRightEncoder().getDistance();
		double heading = Robot.drivetrain.getHeading();

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
		//Robot.drivetrain.arcadeDrive(nextLine[1], nextLine[2]);
		Robot.drivetrain.tankDrive(leftMotorSpeed, rightMotorSpeed, false); //disable squared
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
