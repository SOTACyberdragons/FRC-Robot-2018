package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.SensitivityFilter;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveWithJoysticks extends Command {

	private Preferences prefs = Preferences.getInstance();
	private double moveSensitivityThreshold;
	private double rotateSensitivityThreshold;
	private SensitivityFilter moveSensitivityFilter;
	private SensitivityFilter rotateSensitivityFilter;
	private double rotateSpeed;

	public ArcadeDriveWithJoysticks() {
		super();
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
		Robot.drivetrain.resetSensors();
		
		moveSensitivityThreshold = prefs.getDouble("moveSensitivityThreshold", 0.05);
		rotateSensitivityThreshold = prefs.getDouble("rotateSensitivityThreshold", 0.05);
		rotateSpeed = prefs.getDouble("rotateSpeed", 0.7);
		
		prefs.putDouble("moveSensitivityThreshold", moveSensitivityThreshold);
		prefs.putDouble("rotateSensitivityThreshold", rotateSensitivityThreshold);
		prefs.putDouble("rotateSpeed", rotateSpeed);

		moveSensitivityFilter = new SensitivityFilter(moveSensitivityThreshold);
		rotateSensitivityFilter = new SensitivityFilter(rotateSensitivityThreshold);
	}

	protected void execute() {
		double moveValue = - Robot.oi.getDriveRightStick().getY(); //forward joystick is negative, back is positive
		double rotateValue = Robot.oi.getDriveLeftStick().getX() * rotateSpeed;
		
		SmartDashboard.putNumber("moveValue", moveValue);
		SmartDashboard.putNumber("rotateValue", rotateValue);

		double filteredMoveValue = moveSensitivityFilter.output(moveValue);
		double filteredRotateValue = rotateSensitivityFilter.output(rotateValue);

		if (Robot.isRecording()) {
			Robot.drivetrain.safeArcadeDriveDelayed(filteredMoveValue, filteredRotateValue, 0.01);
		} else {
			Robot.drivetrain.safeArcadeDrive(filteredMoveValue, filteredRotateValue);
		}
		
		//TODO for tank drive calibration for path driving, comment above and uncomment below
//		Robot.drivetrain.normalizedTankDrive(moveValue, moveValue);

	}

	protected boolean isFinished() {
		return false;
	}
}