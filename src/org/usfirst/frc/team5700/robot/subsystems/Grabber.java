package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem {
	
//	private Solenoid piston;
	private Solenoid piston;
	private boolean hasCube;
	
	public Grabber() {
		super();
//		piston = new Solenoid(RobotMap.GRABBER_CHANNEL);
		piston = new Solenoid(RobotMap.GRABBER_CHANNEL);
		close();
	}
	
	public void close() {
//		piston.set(Value.kReverse);
		piston.set(false);
		hasCube = true;
	}
	
	public void open() {
//		piston.set(Value.kForward);
		piston.set(true);
		hasCube = false;
	}
   
    public void initDefaultCommand() {
    }

	public boolean hasCube() {
		return hasCube;
	}
}