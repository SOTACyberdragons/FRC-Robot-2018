package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveElevatorDistance extends Command {

	StringBuilder _sb = new StringBuilder();
	TalonSRX _talon;
	double _targetHeightIn;
	double _endToleranceIn;
	double _delaySec;

    /**
     * Move elevator to target height, command ends when elevator is 
     * at the height +- a tolerance is 0.1 inches, no delay in start of command
     * @param targetHeightIn, height to move elevator to in inches
     */
	public MoveElevatorDistance(double targetHeightIn) {
    	this(targetHeightIn, 0);
    }
    
	/**
     * Move elevator to target height, command ends when elevator is 
     * at the height +- a tolerance is 0.1 inches, command starts after specified delay in seconds.
     * @param targetHeightIn, height to move elevator to in inches
     * @param delaySec, delay until start of command in seconds
     */
	public MoveElevatorDistance(double targetHeightIn, double delaySec) {
    	requires(Robot.elevator);
		_targetHeightIn = targetHeightIn;
		_delaySec = delaySec;
		_endToleranceIn = 0.1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		System.out.print("MED Init");
    		_talon = Robot.elevator.getTalon();
    		Timer.delay(_delaySec);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		Robot.elevator.moveToHeight(_targetHeightIn);

//		try {
//			TimeUnit.MILLISECONDS.sleep(10);
//		} catch (Exception e) {
//		};
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean withinTolerance = (_targetHeightIn - _endToleranceIn) <= Robot.elevator.getHeight() 
        		&& Robot.elevator.getHeight() <= (_targetHeightIn + _endToleranceIn);
        
        System.out.println(withinTolerance);
        return withinTolerance;
    }

    // Called once after isFinished returns true
    protected void end() {
    		System.out.print("MED End");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}