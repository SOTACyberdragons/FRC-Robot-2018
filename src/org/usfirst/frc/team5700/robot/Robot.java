
package org.usfirst.frc.team5700.robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.usfirst.frc.team5700.robot.commands.AutoCenterToLeftSwitch;
import org.usfirst.frc.team5700.robot.commands.AutoCenterToRightSwitch;
import org.usfirst.frc.team5700.robot.commands.AutoCrossBaseline;
import org.usfirst.frc.team5700.robot.commands.AutoDoNotMove;
import org.usfirst.frc.team5700.robot.commands.AutoLeftSideScale;
import org.usfirst.frc.team5700.robot.commands.AutoLeftSideSwitch;
import org.usfirst.frc.team5700.robot.commands.AutoRightSideScale;
import org.usfirst.frc.team5700.robot.commands.ResetArmEncoder;
import org.usfirst.frc.team5700.robot.commands.ResetElevatorEncoder;
import org.usfirst.frc.team5700.robot.commands.AutoRightSideSwitch;
import org.usfirst.frc.team5700.robot.commands.DriveReplay;
import org.usfirst.frc.team5700.robot.commands.ReplayWithCommands;
import org.usfirst.frc.team5700.robot.subsystems.Arm;
import org.usfirst.frc.team5700.robot.subsystems.AssistSystem;
import org.usfirst.frc.team5700.robot.subsystems.Intake;
import org.usfirst.frc.team5700.robot.subsystems.Climber;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.Elevator;
import org.usfirst.frc.team5700.robot.subsystems.Grabber;
import org.usfirst.frc.team5700.utils.CsvLogger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
	private String autoSelected;
	private Command autoCommand;
	public static Preferences prefs;

	SendableChooser<String> chooser;


	public static OI oi;
	public static DriveTrain drivetrain;
	public static Intake intake;
	public static Elevator elevator;
	public static Climber climber; 
	public static Arm arm; 
	public static Grabber grabber;
	public static AssistSystem assistSystem;
	
	public static boolean switchOnRight;
	public static boolean scaleOnRight;
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
	
	public static CsvLogger csvLogger;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		prefs = Preferences.getInstance();
		
		// Initialize all subsystems
		drivetrain = new DriveTrain();
		intake = new Intake();
		elevator = new Elevator();
		climber = new Climber();
		arm = new Arm();
		grabber = new Grabber();
		assistSystem = new AssistSystem();
		oi = new OI();


		// instantiate the command used for the autonomous period
		

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);
		
		//Autonomous Chooser
        chooser = new SendableChooser<String>();
 		chooser.addObject("Dont Move", "Dont Move");
 		chooser.addDefault("Cross Baseline", "Cross Baseline");
 		chooser.addObject("Center Switch", "Center Switch");
 		chooser.addObject("Right Side Switch", "Right Side Switch");
 		chooser.addObject("Left Side Switch", "Left Side Switch");
		chooser.addObject("Replay Test", "Replay Test");
		chooser.addObject("Left Side Switch or Scale", "Left Side Switch or Scale");
 		SmartDashboard.putData("Autonomous Chooser", chooser);
 		SmartDashboard.putData("Autonomous Chooser 2", chooser);
		//autoSelected = chooser.getSelected();
 		
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
		grabber.close();
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
		grabber.close();
		autoSelected = chooser.getSelected();
		
		boolean switchOnRight = true;
		boolean scaleOnRight = true;
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
         if(gameData.length() > 0) {
        	 	if(gameData.charAt(0) == 'L') {
        	 		switchOnRight = false;
        	 	}
        	 	if (gameData.charAt(1) == 'L') {
        	 		scaleOnRight = false;
        	 	}
         }
         
         switch (autoSelected) {
         	case "Dont Move":
         		autoCommand = new AutoDoNotMove();
         		break;
         	case "Cross Baseline":
         		autoCommand = new AutoCrossBaseline();
         		System.out.print("Starting Cross Baseline command");
         		//autoCommand = new AutoRightSideSwitch();
         		break;
         	case "Center Switch":
         		if (switchOnRight) {
         			autoCommand = new AutoCenterToRightSwitch();
         		} else {
         			autoCommand = new AutoCenterToLeftSwitch();
         		}
         		break;
         	case "Right Side Switch":
         		if (switchOnRight) {
         			autoCommand = new AutoRightSideSwitch();
         		} else {
         			autoCommand = new AutoCrossBaseline();
         		}
         		break;
         	case "Left Side Switch":
         		if (!switchOnRight) {
         			autoCommand = new AutoLeftSideSwitch();
         		} else {
         			autoCommand = new AutoCrossBaseline();
         		}
         		break;
         	case "Replay Test":
         		autoCommand = new DriveReplay(replayChooser.getSelected());
         		break;
         	case "Left Side Switch or Scale":
         		if (!scaleOnRight) {
         				autoCommand = new AutoLeftSideScale();
         		} else if (!switchOnRight) {
         			autoCommand = new AutoLeftSideSwitch();
         		} else {
         			autoCommand = new AutoCrossBaseline();
         		}
         		break;
         	default:
         		System.out.print("Starting default command");
         		autoCommand = new AutoCrossBaseline();
         }
         
         //autoCommand = new DriveReplay();
         autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
	
		//Drivetrain
		SmartDashboard.putNumber("Drivetrain speed in per s", drivetrain.getAverageEncoderRate());
		SmartDashboard.putNumber("Right encoder distance", drivetrain.getRightEncoder().getDistance());
		SmartDashboard.putNumber("Left encoder distance", drivetrain.getLeftEncoder().getDistance());
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		SmartDashboard.putData("ReplaySelector", replayChooser);
		SmartDashboard.putData("ReplaySelector 2", replayChooser);
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autoCommand != null)
			autoCommand.cancel();
		
		setupRecordMode();
		listReplays();

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

		//Drivetrain
		SmartDashboard.putNumber("Drivetrain speed in per s", drivetrain.getAverageEncoderRate());
		SmartDashboard.putNumber("Right encoder distance", drivetrain.getRightEncoder().getDistance());
		SmartDashboard.putNumber("Left encoder distance", drivetrain.getLeftEncoder().getDistance());

		SmartDashboard.putNumber("Accelerometer X-axis", drivetrain.getXAccel());
		SmartDashboard.putNumber("Accelerometer Y-axis", drivetrain.getYAccel());
		SmartDashboard.putNumber("Accelerometer Z-axis", drivetrain.getZAccel());
		
		SmartDashboard.putNumber("Gyro Degrees", drivetrain.getHeading());
		
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
		LiveWindow.run();
	}
	
	public static String recordMode() {
		return recordMode;
	}

}
