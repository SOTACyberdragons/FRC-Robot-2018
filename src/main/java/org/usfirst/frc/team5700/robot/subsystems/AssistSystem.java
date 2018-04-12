package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class AssistSystem extends Subsystem {

	private DoubleSolenoid piston; 
	
	public AssistSystem() {
		super();
		piston = new DoubleSolenoid(RobotMap.ASSIST_HOLD_CHANNEL, RobotMap.ASSIST_RELEASE_CHANNEL);
		piston.set(DoubleSolenoid.Value.kForward);
	}

    public void initDefaultCommand() {
    }	
    public void releaseAssist(){
    		piston.set(DoubleSolenoid.Value.kReverse);    	
    }
    public void holdAssist() {
    		piston.set(DoubleSolenoid.Value.kForward);
    }
}

