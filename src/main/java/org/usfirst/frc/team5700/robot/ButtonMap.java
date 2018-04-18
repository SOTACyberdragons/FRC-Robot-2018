package org.usfirst.frc.team5700.robot;

public class ButtonMap {
		
		/**
		 * Primary Driver Buttons
		 */
		//Intake
		public static final int EXTEND_INTAKE = 1; //rightstick
		public static final int SPIN_INTAKE_IN = 2; // //Robot.oi.getDriveRightStick());
		public static final int SPIN_INTAKE_OUT = 2; ////Robot.oi.getDriveLeftStick());
		public static final int VAULT_MODE = 4; //Robot.oi.getDriveRightStick());
		
		/**
		 * Auxiliary Driver Buttons
		 */
		//Grabber 
		public static final int GRABBER_OPEN = 2; //Robot.oi.getAuxRightStick());
		public static final int GRABBER_CLOSE = 1; //Robot.oi.getAuxRightStick());
		
		//Climber
		public static final int CLIMBER_UP = 10; //Robot.oi.getAuxRightStick());
		public static final int CLIMBER_DOWN = 9; //Robot.oi.getAuxRightStick());
		
		//Climber Assist
		public static final int ASSIST_RELEASE = 11; //Robot.oi.getAuxLeftStick());
		
		//Arm
		public static final int MOVE_ARM_TO_90 = 12; // auxLeftStick
		
		//Lifter Placer Automation
		public static final int MOVE_TO_PICK_UP_POSITION = 3; //Robot.oi.getAuxLeftStick());
		public static final int PICK_UP_BOX = 4; //Robot.oi.getAuxLeftStick());
		public static final int MOVE_TO_CRUISE_POSITION = 3; //Robot.oi.getAuxRightStick();
		public static final int MOVE_ELEVATOR_TO_TOP = 4; //Robot.oi.getAuxRightStick();
		public static final int BREAK_BREAM_PICKUP = 6; //Robot.oi.getAuxLeftStick());
		
		/**
		 * Operations Buttons
		 */
		//Zero Encoders
		public static final int ZERO_ARM_ENCODER = 7; //Robot.oi.getAuxLeftStick());
		public static final int ZERO_ELEVATOR_ENCODER = 7; //Robot.oi.getAuxRightStick());
		
		public static final int OVERRIDE_LIMITS = 8; //Robot.oi.getAuxRightStick());
		
}
