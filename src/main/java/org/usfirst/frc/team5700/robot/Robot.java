
package org.usfirst.frc.team5700.robot;

//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Iterator;

import org.usfirst.frc.team5700.robot.Constants.AutoChoice;
import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.commands.AutoCenterSwitch;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaselineCenter;
import org.usfirst.frc.team5700.robot.commands.AutoDoNotMove;
import org.usfirst.frc.team5700.robot.commands.AutoSideScale;
import org.usfirst.frc.team5700.robot.commands.AutoSideSwitch;
//import org.usfirst.frc.team5700.robot.commands.DriveReplay;
import org.usfirst.frc.team5700.robot.subsystems.Arm;
import org.usfirst.frc.team5700.robot.subsystems.AssistSystem;
import org.usfirst.frc.team5700.robot.subsystems.Climber;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5700.robot.subsystems.Elevator;
import org.usfirst.frc.team5700.robot.subsystems.Grabber;
import org.usfirst.frc.team5700.robot.subsystems.Intake;
//import org.usfirst.frc.team5700.utils.CsvLogger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	/** Subsystems **/
	public static OI oi;
	public static Drivetrain drivetrain;
	public static Intake intake;
	public static Elevator elevator;
	public static Climber climber; 
	public static Arm arm; 
	public static Grabber grabber;
	public static AssistSystem assistSystem;
	
	public static Preferences prefs;
	
	private AutoChoice autoChoice;
	private SendableChooser<AutoChoice> chooser;
	private Command autoCommand;

	private static boolean isRecording;
//	String[] data_fields ={
//			"time",
//			"moveValue",
//			"rotateValue",
//			"leftMotorSpeed",
//			"rightMotorSpeed",
//			"speed",
//			"leftSpeed",
//			"rightSpeed",
//			"leftDistance",
//			"rightDistance",
//			"headingError",
//			"moveArmTo90"
//	};

//	private SendableChooser<Boolean> recordModeChooser;
//	private SendableChooser<String> replayChooser;

	/*Auto Commands*/
	//Baseline
	private Command autoCrossBaselineCenter;
	private Command autoCrossBaseline;

	//Center
	private Command autoCenterToRightSwitch;
	private Command autoCenterToLeftSwitch;

	//Right Side
	private Command autoRightSideSwitch;
	private Command autoRightSideScale;

	//Left Side
	private Command autoLeftSideScale;
	private Command autoLeftSideSwitch;

	//Game setup
	private Side switchSide = Side.UNKNOWN;
	private Side scaleSide = Side.UNKNOWN;

//	public static CsvLogger csvLogger;

	private void initCommands() {
		System.out.println("Initializing path commands...");
		
		//Baseline
		autoCrossBaselineCenter = new AutoCrossBaselineCenter();
		autoCrossBaseline = new AutoCrossBaseline();

		//Center
		autoCenterToRightSwitch = new AutoCenterSwitch(Side.RIGHT);
		autoCenterToLeftSwitch = new AutoCenterSwitch(Side.LEFT);

		//Right Side
		autoRightSideSwitch = new AutoSideSwitch(Side.RIGHT);
		autoRightSideScale = new AutoSideScale(Side.RIGHT);

		//Left Side
		autoLeftSideSwitch = new AutoSideSwitch(Side.LEFT);
		autoLeftSideScale = new AutoSideScale(Side.LEFT);
		System.out.println("Done initializing path commands.");
		
		SmartDashboard.putData("autoCrossBaselineCenter", autoCrossBaselineCenter);
		SmartDashboard.putData("autoCrossBaseline", autoCrossBaseline);
		SmartDashboard.putData("autoCenterToRightSwitch", autoCenterToRightSwitch);
		SmartDashboard.putData("autoCenterToLeftSwitch", autoCenterToLeftSwitch);
		SmartDashboard.putData("autoRightSideSwitch", autoRightSideSwitch);
		SmartDashboard.putData("autoRightSideScale", autoRightSideScale);
		SmartDashboard.putData("autoLeftSideSwitch", autoLeftSideSwitch);
		SmartDashboard.putData("autoLeftSideScale", autoLeftSideScale);
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		prefs = Preferences.getInstance();

		// Initialize all subsystems
		drivetrain = new Drivetrain();
		intake = new Intake();
		elevator = new Elevator();
		climber = new Climber();
		arm = new Arm();
		grabber = new Grabber();
		assistSystem = new AssistSystem();
		
		grabber.close();

		oi = new OI();

//		PowerDistributionPanel pdp = new PowerDistributionPanel();

		initCommands();

		//Autonomous Chooser
		chooser = new SendableChooser<AutoChoice>();
		chooser.addObject("Don't Move", AutoChoice.DO_NOT_MOVE);
		chooser.addDefault("Cross Baseline (Sides Only)", AutoChoice.CROSS_BASELINE);
		chooser.addObject("Center Switch", AutoChoice.CENTER_SWITCH);
		chooser.addObject("Right Side Switch Priority", AutoChoice.RIGHT_SWITCH_PRIORITY);
		chooser.addObject("Right Side Scale Priority", AutoChoice.RIGHT_SCALE_PRIORITY);
		chooser.addObject("Left Side Switch Priority", AutoChoice.LEFT_SWITCH_PRIORITY);
		chooser.addObject("Left Side Scale Priority", AutoChoice.LEFT_SCALE_PRIORITY);
//		chooser.addObject("Replay Test", AutoChoice.REPLAY_TEST);
		SmartDashboard.putData("Autonomous Chooser", chooser);

//		setupRecordMode();
//		listReplays();
//		System.out.println("Instantiating CsvLogger...");
//		csvLogger = new CsvLogger();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		grabber.close();
	}

	@Override
	public void disabledPeriodic() {
//		csvLogger.close();
	}

	@Override
	public void autonomousInit() {

//		csvLogger.init(data_fields, Constants.DATA_DIR, false, null);

		autoChoice = chooser.getSelected();
		SmartDashboard.putString("Selected Autonomous", autoChoice.toString());

		switchSide = Side.UNKNOWN;
		scaleSide = Side.UNKNOWN;
		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.length() > 0) {
			if(gameData.charAt(0) == 'L') {
				switchSide = Side.LEFT;
			} else {
				switchSide = Side.RIGHT;
			}
			if (gameData.charAt(1) == 'L') {
				scaleSide = Side.LEFT;
			} else {
				scaleSide = Side.RIGHT;
			}
		}

		SmartDashboard.putString("gameData", gameData);
		SmartDashboard.putString("switchSide", switchSide.toString());
		SmartDashboard.putString("scaleSide", scaleSide.toString());
		
		switch (autoChoice) {
			case DO_NOT_MOVE:
				autoCommand = new AutoDoNotMove();
			case CROSS_BASELINE:
				autoCommand = autoCrossBaseline;
			case CENTER_SWITCH:
				if (switchSide == Side.RIGHT) {
					autoCommand = autoCenterToRightSwitch;
				} else if (switchSide == Side.LEFT) {
					autoCommand = autoCenterToLeftSwitch;
				} else {
					autoCommand = autoCrossBaselineCenter;
				}
				break;
		
			case RIGHT_SCALE_PRIORITY:
				if (scaleSide == Side.RIGHT) {
					autoCommand = autoRightSideScale;
				} else if (switchSide == Side.RIGHT){
					autoCommand = autoRightSideSwitch;
				} else {
					autoCommand = autoCrossBaseline;
				}
				break;
		
			case RIGHT_SWITCH_PRIORITY:
				if (switchSide == Side.RIGHT){
					autoCommand = autoRightSideSwitch;
				} else if (scaleSide == Side.RIGHT) {
					autoCommand = autoRightSideScale;
				} else {
					autoCommand = autoCrossBaseline;
				}
				break;
		
			case LEFT_SCALE_PRIORITY:
				if (scaleSide == Side.LEFT) {
					autoCommand = autoLeftSideScale;
				} else if (switchSide == Side.LEFT){
					autoCommand = autoLeftSideSwitch;
				} else {
					autoCommand = autoCrossBaseline;
				}
				break;
		
			case LEFT_SWITCH_PRIORITY:
				if (switchSide == Side.LEFT){
					autoCommand = autoLeftSideSwitch;
				} else if (scaleSide == Side.LEFT) {
					autoCommand = autoLeftSideScale;
				} else {
					autoCommand = autoCrossBaseline;
				}
				break;
		
//			case REPLAY_TEST:
//				autoCommand = new DriveReplay(replayChooser.getSelected());
//				break;
		
			default:
				//will only work on sides
				autoCommand = autoCrossBaseline;
		}

		SmartDashboard.putString("Autonomous Command", autoCommand.getName());
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		//Elevator
		SmartDashboard.putNumber("Elevator Talon Output", elevator.getTalonOutputVoltage());

		//Arm
		SmartDashboard.putNumber("Arm Raw Angle Deg", arm.getRawAngle());
		SmartDashboard.putNumber("ArmFF", arm.getFeedForward());

		SmartDashboard.putNumber("Right Encoder Distance", drivetrain.getRightEncoder().getDistance());
		SmartDashboard.putNumber("Left Encoder Distance", drivetrain.getLeftEncoder().getDistance());
		SmartDashboard.putNumber("Right Encoder Speed", drivetrain.getRightEncoder().getRate());
		SmartDashboard.putNumber("Left Encoder Speed", drivetrain.getLeftEncoder().getRate());
	}

	@Override
	public void teleopInit() {

		if (autoCommand != null) {
			autoCommand.cancel();
		}
		
		drivetrain.resetSensors();

//		setupRecordMode();
//		listReplays();
//		isRecording = recordModeChooser.getSelected();
//		String newReplayName = SmartDashboard.getString("Replay Name", "MyReplay");
//		csvLogger.init(data_fields, Constants.DATA_DIR, isRecording, newReplayName);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		SmartDashboard.putNumber("Right Encoder Distance", drivetrain.getRightEncoder().getDistance());
		SmartDashboard.putNumber("Left Encoder Distance", drivetrain.getLeftEncoder().getDistance());
		SmartDashboard.putNumber("Right Encoder Speed", drivetrain.getRightEncoder().getRate());
		SmartDashboard.putNumber("Left Encoder Speed", drivetrain.getLeftEncoder().getRate());

		//Intake
		SmartDashboard.putBoolean("Front Break Beam", intake.getFrontBreakBeam());
		SmartDashboard.putBoolean("Back Break Beam", intake.getBackBreakBeam());
		SmartDashboard.putBoolean("In Vault Mode", intake.inVaultMode());

		//Elevator 
		SmartDashboard.putNumber("Elevator Height", elevator.getHeight());
		SmartDashboard.putNumber("Elevator Encoder Ticks", elevator.getEncoderTicks());
		SmartDashboard.putNumber("Elevator Encoder Velocity", elevator.getVelocityTicks());
		SmartDashboard.putNumber("Elevator Talon Output", elevator.getTalonOutputVoltage());
		SmartDashboard.putBoolean("At Bottom Limit ", elevator.atBottomLimit());;
		SmartDashboard.putBoolean("At Top Limit ", elevator.atTopLimit());
		SmartDashboard.putBoolean("At Bottom Limit ", elevator.atBottomLimit());;
		SmartDashboard.putBoolean("At Top Limit ", elevator.atTopLimit());
		SmartDashboard.putBoolean("Limits Overriden ", oi.overrideLimits());

		// Arm
		SmartDashboard.putNumber("ArmFF", arm.getFeedForward());
		SmartDashboard.putNumber("Arm Raw Angle Deg", arm.getRawAngle());
		SmartDashboard.putNumber("ArmFF", arm.getFeedForward());
		SmartDashboard.putNumber("Arm Normalized Angle ", arm.get180NormalizedAngle());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}

	public static boolean isRecording() {
		return isRecording;
	}

//	public static void setGameSide() {
//
//		switchSide = Side.UNKNOWN;
//		scaleSide = Side.UNKNOWN;
//		String gameData;
//		gameData = DriverStation.getInstance().getGameSpecificMessage();
//
//		if (gameData.length() > 0) {
////			gameDataAvailable = true;
//
//			if(gameData.charAt(0) == 'L') {
//				switchSide = Side.LEFT;
//			} else {
//				switchSide = Side.RIGHT;
//			}
//			if (gameData.charAt(1) == 'L') {
//				scaleSide = Side.LEFT;
//			} else {
//				scaleSide = Side.RIGHT;
//			}
//		}
//		SmartDashboard.putString("gameData", gameData);
//		SmartDashboard.putBoolean("gameDataAvailable", gameDataAvailable);
//		SmartDashboard.putString("switchSide", switchSide.toString());
//		SmartDashboard.putString("scaleSide", scaleSide.toString());
//	}
	
//	private void setupRecordMode() {
//		recordModeChooser = new SendableChooser<Boolean>();
//		recordModeChooser.addDefault("Just Drive", false);
//		recordModeChooser.addObject("Record", true);
//		SmartDashboard.putData("RecordMode", recordModeChooser);
//		SmartDashboard.putString("Replay Name", "MyReplay");
//		isRecording = recordModeChooser.getSelected();
//	}

//	private void listReplays() {
//		System.out.println("Listing replays...");
//		replayChooser = new SendableChooser<String>();
//		Iterator<Path> replayFiles = null;
//		try {
//			replayFiles = Files.newDirectoryStream(Paths.get(Constants.DATA_DIR), "*.rpl").iterator();
//			if (replayFiles.hasNext()) {
//				String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
//				replayChooser.addDefault(replayFile, replayFile);
//			}
//			while (replayFiles.hasNext()) {
//				String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
//				System.out.println(replayFile);
//				replayChooser.addObject(replayFile, replayFile);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		SmartDashboard.putData("ReplaySelector", replayChooser);
//	}

}
