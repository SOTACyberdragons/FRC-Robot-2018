package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

	private SpeedController climberMotor = new Spark(RobotMap.CLIMBER_MOTOR);
	
	public Climber() {
		super();
		
		climberMotor.setInverted(true);
	}
	
	public void up(double speed) {
		climberMotor.set(speed);
	}

	public void down(double speed) {
		climberMotor.set(speed);
		
	}
	
	public void stop() {
		climberMotor.set(0.0);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

