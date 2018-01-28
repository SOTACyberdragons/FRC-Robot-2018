package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Grabber extends Subsystem {
	
	public Compressor compressor = new Compressor();
	
	private DoubleSolenoid piston;
	
	public Grabber() {
		super();
		piston = new DoubleSolenoid(RobotMap.GRABBER_FORWARD_CHANNEL, RobotMap.GRABBER_BACKWARD_CHANNEL);
		
		piston.set(DoubleSolenoid.Value.kForward); 
	}
	
	public void grabberOpen() {
		piston.set(DoubleSolenoid.Value.kReverse); 
	}
	
	public void grabberClose() {
		piston.set(DoubleSolenoid.Value.kForward);
	}
   
    public void initDefaultCommand() {
      
    }
    
    public void log(){
    	}
}

