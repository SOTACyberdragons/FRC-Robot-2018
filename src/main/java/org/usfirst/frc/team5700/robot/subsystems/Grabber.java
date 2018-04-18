package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem {
	
//	private Solenoid piston;
	private DoubleSolenoid piston;
	private boolean hasCube;
	
	public Grabber() {
		super();
//		piston = new Solenoid(RobotMap.GRABBER_CHANNEL);
		piston = new DoubleSolenoid(RobotMap.CLOSE_GRABBER_CHANNEL, RobotMap.OPEN_GRABBER_CHANNEL);
		close();
	}
	
	public void close() {
		piston.set(Value.kReverse);
//		piston.set(true);
		hasCube = true;
	}
	
	public void open() {
		piston.set(Value.kForward);
//		piston.set(false);
		hasCube = false;
	}
   
    public void initDefaultCommand() {
    }

	public boolean hasCube() {
		return hasCube;
	}
}