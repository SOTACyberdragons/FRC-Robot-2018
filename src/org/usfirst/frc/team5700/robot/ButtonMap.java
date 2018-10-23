package org.usfirst.frc.team5700.robot;

public class ButtonMap {
		
		/**
		 * Primary Driver Buttons
		 */
		//Intake
		public static final int EXTEND_INTAKE = 1; // driveRightstick
		public static final int SPIN_INTAKE_IN = 2; // driveRightStick
		public static final int SPIN_INTAKE_OUT = 2; // driveLeftStick
		public static final int AUX_EXTEND_INT1AKE = 1; // auxLeftStick
		public static final int VAULT_MODE = 4; // driveRightStick
		/**
		 * Auxiliary Driver Buttons
		 */
		//Grabber 
		public static final int GRABBER_OPEN = 2; // auxRightStick
		public static final int GRABBER_CLOSE = 1; // auxRightStick
		//public static final int DRIVE_GRABBER_OPEN = 1; // driveLeftStick
		
		//Climber
		public static final int CLIMBER_UP = 10; // auxRightStick
		public static final int CLIMBER_DOWN = 9; // auxRightStick
		
		//Climber Assist
		public static final int ASSIST_RELEASE = 11; // auxLeftStick
		
		//Arm
		public static final int MOVE_ARM_TO_90 = 12; // auxLeftStick
		
		//Lifter Placer Automation
		public static final int MOVE_TO_PICK_UP_POSITION = 3; // auxLeftStick
		public static final int PICK_UP_BOX = 4; // auxLeftStick
		public static final int MOVE_TO_CRUISE_POSITION = 3; // auxRightStick
		public static final int MOVE_ELEVATOR_TO_SWITCH = 5; // auxRightStick
		public static final int MOVE_ELEVATOR_TO_TOP = 4; // auxRightStick
		public static final int BREAK_BREAM_PICKUP = 6; // auxLeftStick
		
		/**
		 * Operations Buttons
		 */
		//Zero Encoders
		public static final int RESET_ARM_ENCODER = 7; // auxLeftStick
		public static final int ZERO_ELEVATOR_ENCODER = 7; // auxRightStick
		
		public static final int OVERRIDE_LIMITS = 8; // auxRightStick
	
}
