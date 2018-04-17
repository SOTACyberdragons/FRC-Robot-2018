package org.usfirst.frc.team5700.robot;

public class Constants {

	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int TIMEOUT_MS = 10;

	/**
	 * Talon
	 */
	public static final int TALON_MAX_OUTPUT = 1023;
	public static final int VERSA_ENCODER_TPR = 4096;
	
	/**
	 * Roborio
	 */	
	public static final double CYCLE_SEC = 0.020; //20 milliseconds
	public static final String DATA_DIR = "/home/lvuser/data_capture/";
	public static final String PATHFINDER_DIR = "/home/lvuser/pathfinder/";
}
