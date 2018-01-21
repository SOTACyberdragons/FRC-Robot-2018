package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BoxIntake extends Subsystem {
	private SpeedController intakeMotor = new Spark(RobotMap.INTAKE_MOTOR);
	
	private double intakeSpeed = 0.8; 
	
//	public BoxIntake() {
//		intakeMotor = new Spark(RobotMap.INTAKE_MOTOR);
//	}
	
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public void setIntakeSpeed(double speed) {
		intakeMotor.set(speed);
	}
	
	public void intakeBox() {
		intakeMotor.set(intakeSpeed);
	}

	public void stopBoxIntake() {
		intakeMotor.set(0.0);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
    }
}

