package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {
	
	private SpeedController elevatorMotor = new Spark(RobotMap.ELEVATION_MOTOR_GIVES_YOU_LOTS_OF_LIFT_YUM_YUM);
	
	public double elevatorSpeedUp = 0.9;
	public double elevatorSpeedDown = -0.9;
	
	public void elevatorSpeedUp(double speed) {
		elevatorMotor.set(speed);
		
	}
	
	public void elevatorSpeedDown(double speed) {
		elevatorMotor.set(speed);
		
	}
	
	public void elevatorGoUp() {
		elevatorMotor.set(elevatorSpeedUp);
		
	}

	public void elevatorGoDown() {
		elevatorMotor.set(elevatorSpeedDown);
		
	}
	
	public void setElevatorMotor(double speed) {
		elevatorMotor.set(speed);
	}
	
	public void stopElevator() {
		elevatorMotor.set(0.0);
	}
	
	

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

