package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.BreakBeamPickup;
import org.usfirst.frc.team5700.robot.commands.ClimberDown;
import org.usfirst.frc.team5700.robot.commands.ClimberUp;
import org.usfirst.frc.team5700.robot.commands.ExtendIntake;
import org.usfirst.frc.team5700.robot.commands.GrabCube;
import org.usfirst.frc.team5700.robot.commands.ReleaseCube;
import org.usfirst.frc.team5700.robot.commands.ResetArmEncoder;
import org.usfirst.frc.team5700.robot.commands.IntakeBox;
import org.usfirst.frc.team5700.robot.commands.IntakeSpinIn;
import org.usfirst.frc.team5700.robot.commands.IntakeSpitOut;
import org.usfirst.frc.team5700.robot.commands.MoveArmAndElevatorDistance;
import org.usfirst.frc.team5700.robot.commands.MoveArmToAngle;
import org.usfirst.frc.team5700.robot.commands.MoveElevatorWithJoystick;
import org.usfirst.frc.team5700.robot.commands.MoveElevatorDistance;
import org.usfirst.frc.team5700.robot.commands.PickupCube;
import org.usfirst.frc.team5700.robot.commands.ReleaseAssistArm;
import org.usfirst.frc.team5700.robot.commands.ResetElevatorEncoder;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI_practice {

	private boolean limitsAreOverriden = false;
	private boolean hasBeenPressed = false;
	
	private Joystick driveRightStick = new Joystick(0);
	private Joystick driveLeftStick = new Joystick(1);
	private Joystick auxRightStick = new Joystick(2);
	private Joystick auxLeftStick = new Joystick(3);
	
	// Setting squaredInput to true decreases the sensitivity for tankdrive at lower speeds
	private boolean squaredInput = true;
	//Intake
	JoystickButton spinIntakeIn;
	JoystickButton spitIntakeOut;
	JoystickButton extendIntake;
	
	//Grabber
	JoystickButton releaseCube;
	JoystickButton grabCube;
	
	//Climber
	JoystickButton climberUp;
	JoystickButton climberDown;
	
	//Arm
	JoystickButton moveArmTo90;
	
	//Elevator
	//JoystickButton moveElevatorDistance;
	
	//Assist 
	JoystickButton releaseAssist;
	
	//Lifter Automation Buttons
	JoystickButton moveToPickUpPosition;
	JoystickButton pickupCube;
	JoystickButton moveToCruise;
	JoystickButton moveElevatorToTop;
	JoystickButton breakBeamPickup;


	//Operations Buttons
	JoystickButton zeroElevatorEncoder;
	JoystickButton zeroArmEncoder;
	JoystickButton overrideLimits;
	public JoystickButton vaultMode;
	
	public OI_practice() {
		/**
		 * Set Buttons
		 */
		//Intake
		spinIntakeIn = new JoystickButton (driveRightStick, ButtonMap.SPIN_INTAKE_IN);
		spitIntakeOut = new JoystickButton (driveLeftStick, ButtonMap.SPIN_INTAKE_OUT);
		extendIntake = new JoystickButton (driveRightStick, ButtonMap.EXTEND_INTAKE);
		vaultMode = new JoystickButton(driveRightStick, ButtonMap.VAULT_MODE);
		
		//Grabber
		releaseCube = new JoystickButton(auxRightStick, ButtonMap.GRABBER_OPEN);
		grabCube = new JoystickButton(auxRightStick, ButtonMap.GRABBER_CLOSE);
		
		//Climber
		climberUp = new JoystickButton(auxRightStick, ButtonMap.CLIMBER_UP);
		climberDown = new JoystickButton(auxRightStick, ButtonMap.CLIMBER_DOWN);
		
		//Arm
		moveArmTo90 = new JoystickButton(auxLeftStick, ButtonMap.MOVE_ARM_TO_90);
		
		//Elevator
		//moveElevatorDistance = new JoystickButton(auxLeftStick, ButtonMap.MOVE_ELEVATOR_DISTANCE);
		
		//Assist
		releaseAssist = new JoystickButton(auxLeftStick, ButtonMap.ASSIST_RELEASE);
		
		//Lifter Automation Buttons
		moveToPickUpPosition = new JoystickButton(auxLeftStick, ButtonMap.MOVE_TO_PICK_UP_POSITION);
		pickupCube = new JoystickButton(auxLeftStick, ButtonMap.PICK_UP_BOX);
		moveToCruise = new JoystickButton(auxRightStick, ButtonMap.MOVE_TO_CRUISE_POSITION);
		moveElevatorToTop = new JoystickButton(auxRightStick, ButtonMap.MOVE_ELEVATOR_TO_TOP);
		breakBeamPickup = new JoystickButton(auxLeftStick, ButtonMap.BREAK_BREAM_PICKUP);
		
		//Operations Buttons
		zeroElevatorEncoder  = new JoystickButton(auxRightStick, ButtonMap.ZERO_ELEVATOR_ENCODER);
		zeroArmEncoder = new JoystickButton(auxLeftStick, ButtonMap.ZERO_ARM_ENCODER);
		overrideLimits = new JoystickButton(auxRightStick, ButtonMap.OVERRIDE_LIMITS);
		
		
		/**
		 * Set Commands
		 */
		//box intake
//		spinIntakeIn.whileHeld(new IntakeSpinIn());
//		extendIntake.whileHeld(new ExtendIntake());
//		spitIntakeOut.whileHeld(new IntakeSpitOut());
//		
//		//grabber
//		grabCube.whenPressed(new GrabCube());
//		releaseCube.whenPressed(new ReleaseCube());
//		
//		//climber
//		climberUp.whileHeld(new ClimberUp());
//		climberDown.whileHeld(new ClimberDown());
//		
//		//arm
//		moveArmTo90.whileHeld(new MoveArmToAngle(90));
//		
//		//elevator
//		//moveElevatorDistance.whileHeld(new MoveElevatorDistance(30));
//		
//		//climber assist
//		releaseAssist.whileHeld(new ReleaseAssistArm());
//		
//		//Lifter Automation Buttons
//		moveToPickUpPosition.whileHeld(new MoveArmAndElevatorDistance(16.5, 0));
//		pickupCube.whenPressed(new PickupCube());
//		moveToCruise.whileHeld(new MoveArmAndElevatorDistance(2, 180, 0.5, 0));
//		moveElevatorToTop.whileHeld(new MoveElevatorDistance(58));
//		breakBeamPickup.whileHeld(new BreakBeamPickup());
//				
//		//Operations Buttons
//		zeroElevatorEncoder.whenPressed(new ResetElevatorEncoder());
//		zeroArmEncoder.whenPressed(new ResetArmEncoder());
	}
	
	public Joystick getDriveLeftStick() {
		return driveLeftStick;	
	}

	public Joystick getDriveRightStick() {
		return driveRightStick;
	}
	
	public Joystick getAuxLeftStick() {
		return auxLeftStick;	
	}

	public Joystick getAuxRightStick() {
		return auxRightStick;
	}

	public boolean getSquaredInput() {
		return squaredInput;
	}
	
	public double getRightStickThrottle() {
		return (driveRightStick.getThrottle() + 1)/2.0;
	}
	
	public boolean overrideLimits() {
		if (overrideLimits.get() && !hasBeenPressed) {
			limitsAreOverriden = !limitsAreOverriden;
			hasBeenPressed = true;
		}
		
		if(!overrideLimits.get()) {
			hasBeenPressed = false;
		}
		return limitsAreOverriden;
	}

}


