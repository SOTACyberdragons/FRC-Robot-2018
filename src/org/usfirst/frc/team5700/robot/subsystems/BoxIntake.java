package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BoxIntake extends Subsystem {

	public Compressor compressor = new Compressor();
	
	private DoubleSolenoid leftPiston, rightPiston;
	
    public BoxIntake() {
		super();
	    	leftPiston = new DoubleSolenoid(0, 1);
	    	leftPiston.set(DoubleSolenoid.Value.kForward);
	    	
	    	rightPiston = new DoubleSolenoid(2, 3);
	    	rightPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void boxIntakeOut() {
		leftPiston.set(DoubleSolenoid.Value.kReverse);
		rightPiston.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void boxIntakeIn() {
		leftPiston.set(DoubleSolenoid.Value.kForward);
		rightPiston.set(DoubleSolenoid.Value.kForward);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

