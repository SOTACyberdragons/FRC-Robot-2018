package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.SensitivityFilter;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveWithJoysticks extends Command {

	private Preferences m_prefs = Preferences.getInstance();
	private double m_moveSensitivityThreshold;
	private double m_rotateSensitivityThreshold;

	public ArcadeDriveWithJoysticks() {
		super();
		requires(Robot.drivetrain);
	}

	@Override
	protected void initialize() {
		Robot.drivetrain.resetSensors();
	}

	protected void execute() {
		double moveValue = - Robot.oi.getDriveRightStick().getY(); //forward joystick is negative, back is positive
		double rotateValue = - Robot.oi.getDriveLeftStick().getX() * 0.7;
		SmartDashboard.putNumber("moveValue", moveValue);
		SmartDashboard.putNumber("rotateValue", rotateValue);

		m_moveSensitivityThreshold = m_prefs.getDouble("moveSensitivityThreshold", 0.05);
		m_rotateSensitivityThreshold = m_prefs.getDouble("rotateSensitivityThreshold", 0.05);

		SensitivityFilter moveSensitivityFilter = new SensitivityFilter(m_moveSensitivityThreshold);
		SensitivityFilter rotateSensitivityFilter = new SensitivityFilter(m_rotateSensitivityThreshold);

		double filteredMoveValue = moveSensitivityFilter.output(moveValue);
		double filteredRotateValue = rotateSensitivityFilter.output(rotateValue);

		if (Robot.recordMode().equals("replay"))
			Robot.drivetrain.safeArcadeDriveDelayed(filteredMoveValue,
					filteredRotateValue, 0.01);
		else
			Robot.drivetrain.safeArcadeDrive(filteredMoveValue,
					filteredRotateValue);
//			Robot.drivetrain.boostedTankDrive(filteredMoveValue, filteredMoveValue);

	}

	protected boolean isFinished() {
		return false;
	}
}