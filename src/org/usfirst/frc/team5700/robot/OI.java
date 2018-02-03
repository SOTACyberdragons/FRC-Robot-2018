package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.IntakeBox;
import org.usfirst.frc.team5700.robot.commands.ReverseIntakeBox;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private boolean toggle = false;
	private boolean hasBeenPressed = false;

	private Joystick rightStick = new Joystick(0);
	private Joystick leftStick = new Joystick(1);

	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;

	JoystickButton slowDrive;
	JoystickButton toggleDirection;
	JoystickButton intakeBox;
	JoystickButton reverseIntakeBox;
	//JoystickButton orientBox;

	public OI() {
		slowDrive = new JoystickButton(rightStick, ButtonMap.SLOW_DRIVE);
		toggleDirection = new JoystickButton(rightStick, ButtonMap.TOGGLE_DIRECTION);
		intakeBox = new JoystickButton(rightStick, ButtonMap.INTAKE_BOX);
		reverseIntakeBox = new JoystickButton(leftStick, ButtonMap.REVERSE_INTAKE_BOX);
		//orientBox = new JoystickButton(leftStick, ButtonMap.ORIENT_BOX);
		
		//set commands
		//box intake
		intakeBox.whileHeld(new IntakeBox());
		reverseIntakeBox.whileHeld(new ReverseIntakeBox());
	}

	public Joystick getLeftStick() {
		return leftStick;	
	}

	public Joystick getRightStick() {
		return rightStick;
	}

	public boolean getSquaredInput() {
		return squaredInput;
	}

	public boolean driveSlow() {
		return slowDrive.get();
	}
	
	/*public boolean getOrientBoxButton() {
		return orientBox.get();
	}
	*/
	
	public boolean directionToggle() {
		if (toggleDirection.get() && !hasBeenPressed) {
			toggle = !toggle;
			hasBeenPressed = true;
		}

		if(!toggleDirection.get()) {
			hasBeenPressed = false;
		}
		return toggle;
	}
}


