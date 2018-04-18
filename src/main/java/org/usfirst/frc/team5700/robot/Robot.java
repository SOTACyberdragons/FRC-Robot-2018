
package org.usfirst.frc.team5700.robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.Constants.AutoChoice;
import org.usfirst.frc.team5700.robot.Constants.Side;
import org.usfirst.frc.team5700.robot.Constants.StartPosition;
import org.usfirst.frc.team5700.robot.commands.AutoCenterSwitch;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaselineCenter;
import org.usfirst.frc.team5700.robot.commands.AutoDoNotMove;
import org.usfirst.frc.team5700.robot.commands.AutoSideScale;
import org.usfirst.frc.team5700.robot.commands.AutoSideSwitch;
import org.usfirst.frc.team5700.robot.commands.DriveReplay;
import org.usfirst.frc.team5700.robot.subsystems.Arm;
import org.usfirst.frc.team5700.robot.subsystems.AssistSystem;
import org.usfirst.frc.team5700.robot.subsystems.Climber;
import org.usfirst.frc.team5700.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5700.robot.subsystems.Elevator;
import org.usfirst.frc.team5700.robot.subsystems.Grabber;
import org.usfirst.frc.team5700.robot.subsystems.Intake;
import org.usfirst.frc.team5700.utils.CsvLogger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
	private AutoChoice autoChoice;
	private Command autoCommand;
	public static Preferences prefs;

	SendableChooser<AutoChoice> chooser;


	public static OI oi;
	public static Drivetrain drivetrain;
	public static Intake intake;
	public static Elevator elevator;
	public static Climber climber; 
	public static Arm arm; 
	public static Grabber grabber;
	public static AssistSystem assistSystem;
	
	public static boolean dropCube = false;
	
	String[] data_fields ={
			"time",
			"moveValue",
			"rotateValue",
			"leftMotorSpeed",
			"rightMotorSpeed",
			"speed",
			"leftSpeed",
			"rightSpeed",
			"leftDistance",
			"rightDistance",
			"headingError",
			"moveArmTo90"
	};
	
	private SendableChooser<String> recordModeChooser;
	private static String recordMode;
	private SendableChooser<String> replayChooser;
	
	
	/*Auto Commands*/
	//Baseline
	private static Command autoCrossBaselineCenter;
	private static Command autoCrossBaseline;
	
	//Center
	private static Command autoCenterToRightSwitch;
	private static Command autoCenterToLeftSwitch;
	
	//Right Side
	private static Command autoRightSideSwitch;
	private static Command autoRightSideScale;
	
	//Left Side
	private static Command autoLeftSideScale;
	private static Command autoLeftSideSwitch;
	
	
	public static CsvLogger csvLogger;
	
	private static void initPathCommands() {
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
		autoLeftSideScale = new AutoSideSwitch(Side.LEFT);
		autoLeftSideScale = new AutoSideScale(Side.LEFT);
	}
	
	private static Side switchSide;
	private static Side scaleSide;
	private static StartPosition startPosition;
	private static boolean gameDataAvailable;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		prefs = Preferences.getInstance();
		
		// Initialize all subsystems
		drivetrain = new Drivetrain();
		
		//TODO: uncomment on competition robot //TODID
		intake = new Intake();
		elevator = new Elevator();
		climber = new Climber();
		arm = new Arm();
		grabber = new Grabber();
		assistSystem = new AssistSystem();
		
		oi = new OI();
		
		@SuppressWarnings("unused")
		PowerDistributionPanel pdp = new PowerDistributionPanel();

		initPathCommands();
		SmartDashboard.putData("Center To Right Switch", autoCenterToRightSwitch);
		SmartDashboard.putData("Center To Left Switch", autoCenterToLeftSwitch);
		

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		
		//Autonomous Chooser
        chooser = new SendableChooser<AutoChoice>();
 		chooser.addObject("Dont Move", AutoChoice.DO_NOT_MOVE);
 		chooser.addDefault("Cross Baseline", AutoChoice.CROSS_BASELINE);
 		chooser.addObject("Center Switch", AutoChoice.CENTER_SWITCH);
 		chooser.addObject("Right Side Switch Priority", AutoChoice.RIGHT_SWITCH_PRIORITY);
 		chooser.addObject("Right Side Scale Priority", AutoChoice.RIGHT_SCALE_PRIORITY);
 		chooser.addObject("Left Side Switch Priority", AutoChoice.LEFT_SWITCH_PRIORITY);
 		chooser.addObject("Left Side Scale Priority", AutoChoice.LEFT_SCALE_PRIORITY);
		chooser.addObject("Replay Test", AutoChoice.REPLAY_TEST);
 		SmartDashboard.putData("Autonomous Chooser", chooser);
 		
		setupRecordMode();
		listReplays();
 		
 		grabber.close();
 		
		System.out.println("Instantiating CsvLogger...");
		csvLogger = new CsvLogger();
	}
	
	private void setupRecordMode() {
		recordModeChooser = new SendableChooser<String>();
		recordModeChooser.addDefault("Just Drive", "justDrive");
		recordModeChooser.addObject("Replay", "replay");
		SmartDashboard.putData("RecordMode", recordModeChooser);
		SmartDashboard.putData("RecordMode 2", recordModeChooser);
		SmartDashboard.putString("Replay Name", "MyReplay");
		recordMode = recordModeChooser.getSelected();
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
		csvLogger.close();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
		csvLogger.init(data_fields, Constants.DATA_DIR, false, null);
		
		dropCube = false;
//		grabber.close();
		autoChoice = chooser.getSelected();
		
		setGameSide();
        
		if (autoChoice == AutoChoice.DO_NOT_MOVE) {
			autoCommand = new AutoDoNotMove();
			
		} else if (!gameDataAvailable || autoChoice == AutoChoice.CROSS_BASELINE) {
			
			if (startPosition == StartPosition.CENTER) {
				autoCommand = autoCrossBaselineCenter;
				
			} else {
				autoCommand = autoCrossBaseline;
			}
		} else {
	         switch (autoChoice) {
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
	         		
	         	case REPLAY_TEST:
	         		autoCommand = new DriveReplay(replayChooser.getSelected());
	         		break;
	         		
	         	default:
	         		if (startPosition == StartPosition.CENTER) {
	    				autoCommand = autoCrossBaselineCenter;
	    				
	    			} else {
	    				autoCommand = autoCrossBaseline;
	    			}
	         }
		}

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
	}
	
	private void listReplays() {
		System.out.println("Listing replays...");
		replayChooser = new SendableChooser<String>();
		Iterator<Path> replayFiles = null;
		try {
			replayFiles = Files.newDirectoryStream(Paths.get(Constants.DATA_DIR), "*.rpl").iterator();
			if (replayFiles.hasNext()) {
				String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
				replayChooser.addDefault(replayFile, replayFile);
			}
			while (replayFiles.hasNext()) {
				String replayFile = replayFiles.next().getFileName().toString().replaceFirst("[.][^.]+$", "");
				System.out.println(replayFile);
				replayChooser.addObject(replayFile, replayFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		SmartDashboard.putData("ReplaySelector", replayChooser);
	}

	@Override
	public void teleopInit() {
		
		if (autoCommand != null)
			autoCommand.cancel();
		
		setupRecordMode();
		listReplays();
		drivetrain.resetSensors();

		recordMode = recordModeChooser.getSelected();

		String newReplayName = SmartDashboard.getString("Replay Name", "MyReplay");
		csvLogger.init(data_fields, Constants.DATA_DIR, recordMode.equals("replay"), newReplayName);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();	
		
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
	
	public static String recordMode() {
		return recordMode;
	}
	
	public static void setGameSide() {
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
        
		if (gameData.length() > 0) {
			gameDataAvailable = true;
			
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
	}

}
