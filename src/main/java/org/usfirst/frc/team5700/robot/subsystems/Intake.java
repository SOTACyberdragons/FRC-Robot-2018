package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	
	private final static double INTAKE_SPEED = -0.6;
	private final static double SPINOUT_SPEED = -1.0;
	
	private Solenoid solenoid;
	Spark intakeMotors;
	
	//practice bot
//	private DoubleSolenoid rightDoubleSolenoid, leftDoubleSolenoid;
	
	private boolean inVaultMode = false;
	public DigitalInput frontBreakBeam = new DigitalInput(RobotMap.FRONT_BREAK_BEAM_CHANNEL);
	public DigitalInput backBreakBeam = new DigitalInput(RobotMap.BACK_BREAK_BEAM_CHANNEL);
	private boolean vaultButtonBeenPressed = false; 
	
    public Intake() {
		super();
	    solenoid = new Solenoid(RobotMap.EXTEND_INTAKES_CHANNEL);
//		rightDoubleSolenoid = new DoubleSolenoid(RobotMap.EXTEND_RIGHT_INTAKES_CHANNEL, RobotMap.CLOSE_RIGHT_INTAKES_CHANNEL);
//		leftDoubleSolenoid = new DoubleSolenoid(RobotMap.EXTEND_LEFT_INTAKES_CHANNEL, RobotMap.CLOSE_LEFT_INTAKES_CHANNEL);
	    intakeMotors = new Spark(RobotMap.INTAKE_MOTORS);
    }
    
    public void extendBoth(){
//    	rightDoubleSolenoid.set(Value.kForward);
//    	leftDoubleSolenoid.set(Value.kForward);
    	solenoid.set(true);
    }
    
    public void retractBoth(){
//    	rightDoubleSolenoid.set(Value.kReverse);
//    	leftDoubleSolenoid.set(Value.kReverse);
		solenoid.set(false);
    }
    
	//These methods are for spitting out a box.
	public void spinBothMotorsOut(){
		intakeMotors.set(SPINOUT_SPEED);
	}
	
	//these methods are for pulling in the box (intaking).
	public void spinMotorsIn() {
		intakeMotors.set(-INTAKE_SPEED);
	}
	
	//this method is for stopping the intake motors.
	public void stopMotors() {
		intakeMotors.set(0);
	}
	
	public boolean getFrontBreakBeam() {
		return !frontBreakBeam.get();
	}
	
	public boolean getBackBreakBeam() {
		return !backBreakBeam.get();
	}
	
	public boolean inVaultMode() {
		if (Robot.oi.vaultMode.get() && !vaultButtonBeenPressed) {
			inVaultMode = !inVaultMode;
			vaultButtonBeenPressed = true;
		}
		
		if(!Robot.oi.vaultMode.get()) {
			vaultButtonBeenPressed = false;
		}
		
		return inVaultMode;
	}

	@Override
	protected void initDefaultCommand() {
	}
		
}