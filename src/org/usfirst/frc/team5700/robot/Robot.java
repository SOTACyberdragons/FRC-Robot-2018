
package org.usfirst.frc.team5700.robot;

import org.usfirst.frc.team5700.robot.commands.ResetArmEncoder;
import org.usfirst.frc.team5700.robot.commands.ResetElevatorEncoder;
import org.usfirst.frc.team5700.robot.subsystems.Arm;
import org.usfirst.frc.team5700.robot.subsystems.AssistSystem;
import org.usfirst.frc.team5700.robot.subsystems.BoxIntake;
import org.usfirst.frc.team5700.robot.subsystems.Climber;
import org.usfirst.frc.team5700.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5700.robot.subsystems.Elevator;
import org.usfirst.frc.team5700.robot.subsystems.Grabber;

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
	private Command autonomousCommand;
	public static Preferences prefs;

	SendableChooser<Command> chooser;


	public static OI oi;
	public static DriveTrain drivetrain;
	public static BoxIntake boxIntake;
	public static Elevator elevator;
	public static Climber climber; 
	public static Arm arm; 
	public static Grabber grabber;
	public static AssistSystem assistSystem;



	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		prefs = Preferences.getInstance();
		
		// Initialize all subsystems
		drivetrain = new DriveTrain();
		boxIntake = new BoxIntake();
		elevator = new Elevator();
		climber = new Climber();
		arm = new Arm();
		grabber = new Grabber();
		assistSystem = new AssistSystem();
		oi = new OI();


		// instantiate the command used for the autonomous period
		

		// Show what command your subsystem is running on the SmartDashboard
		SmartDashboard.putData(drivetrain);

		SmartDashboard.putData("Reset Elevator Encoder", new ResetElevatorEncoder());	
		SmartDashboard.putData("Reset Arm Encoder", new ResetArmEncoder());
		

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
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
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();	
		SmartDashboard.putNumber("Accelerometer X-axis", drivetrain.getXAccel());
		SmartDashboard.putNumber("Accelerometer Y-axis", drivetrain.getYAccel());
		SmartDashboard.putNumber("Accelerometer Z-axis", drivetrain.getZAccel());
		SmartDashboard.putNumber("Elevator Talon Output", elevator.getTalonOutputVolatage());
		SmartDashboard.putNumber("Arm Encoder Ticks", arm.getRawEncoderTicks());
		SmartDashboard.putNumber("Arm Angle Deg", arm.getAngle());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
