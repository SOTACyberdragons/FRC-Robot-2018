package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.ClimberDown;
import org.usfirst.frc.team5700.robot.commands.ClimberUp;
import org.usfirst.frc.team5700.robot.commands.CloseForIntake;
import org.usfirst.frc.team5700.robot.commands.DingusGo;
import org.usfirst.frc.team5700.robot.commands.ExtendLeft;
import org.usfirst.frc.team5700.robot.commands.ExtendRight;
import org.usfirst.frc.team5700.robot.commands.IntakeBox;
import org.usfirst.frc.team5700.robot.commands.IntakeSpinIn;
import org.usfirst.frc.team5700.robot.commands.IntakeSpinOut;
import org.usfirst.frc.team5700.robot.commands.IntakeSpitAndExtend;
import org.usfirst.frc.team5700.robot.commands.IntakeSpitOut;
import org.usfirst.frc.team5700.robot.commands.ReleaseAssist;

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

	private Joystick rightstick = new Joystick(0);
	private Joystick leftstick = new Joystick(1);

	
	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;

	JoystickButton slowDrive;
	JoystickButton toggleDirection;
	JoystickButton extendRight;
	JoystickButton extendLeft;
	JoystickButton intakeBox;
	JoystickButton spitAndExtend;
	JoystickButton climberUp;
	JoystickButton climberDown;
	JoystickButton dingusGo; 
	JoystickButton releaseAssist;
	JoystickButton closeForIntake;
	JoystickButton intakeSpinOut;
	public OI() {
		slowDrive = new JoystickButton(rightstick, ButtonMap.SLOW_DRIVE);
		toggleDirection = new JoystickButton(rightstick, ButtonMap.TOGGLE_DIRECTION);
		extendRight = new JoystickButton(rightstick, ButtonMap.EXTEND_RIGHT);
		extendLeft = new JoystickButton(rightstick, ButtonMap.EXTEND_LEFT);
		intakeBox = new JoystickButton (rightstick, ButtonMap.INTAKE_BOX);
		spitAndExtend = new JoystickButton (leftstick, ButtonMap.SPIT_AND_EXTEND);
		climberUp = new JoystickButton(rightstick, ButtonMap.CLIMBER_UP);
		climberDown = new JoystickButton(rightstick, ButtonMap.CLIMBER_DOWN);
		dingusGo = new JoystickButton(rightstick, ButtonMap.DINGUS_GO);
		releaseAssist = new JoystickButton(rightstick, ButtonMap.ASSIST_RELEASE);
		closeForIntake = new JoystickButton(leftstick, ButtonMap.CLOSE_FOR_INTAKE);
		intakeSpinOut = new JoystickButton(leftstick, ButtonMap.SPIN_INTAKE_OUT);
		//set commands
		//box intake
		intakeBox.whileHeld(new IntakeBox());
		extendLeft.whileHeld(new ExtendLeft());
		extendRight.whileHeld(new ExtendRight());
		spitAndExtend.whileHeld(new IntakeSpitAndExtend());
		dingusGo.whileHeld(new DingusGo());
		closeForIntake.whileHeld(new CloseForIntake());
		intakeSpinOut.whileHeld(new IntakeSpinOut());

		//climber
		climberUp.whileHeld(new ClimberUp());
		climberDown.whileHeld(new ClimberDown());
		//climber assist
		releaseAssist.whileHeld(new ReleaseAssist());
		
	}
	
	public Joystick getLeftstick() {
		return leftstick;	
	}

	public Joystick getRightstick() {
		return rightstick;
	}
	
	public boolean getSquaredInput() {
		return squaredInput;
	}

	public boolean driveSlow() {
		return slowDrive.get();
	}
	

	
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


