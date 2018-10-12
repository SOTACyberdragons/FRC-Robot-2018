package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveArmToAngle extends Command {

    private double _targetAngleDeg;
    private double _delaySec;
	private double _endToleranceDeg;
	
	/**
	 * Moves arm to specified angle with no delay until start. Angle is absolute, 
	 * between 0 when the arm is facing straight down, angle increasing as the 
	 * arm rotates counterclockwise. 
	 * @param angleDeg, angle between 0 inclusive and 360 exclusive.
	 */
    public MoveArmToAngle(double angleDeg) {
        requires(Robot.arm);
        _endToleranceDeg = 1;
        _targetAngleDeg = angleDeg;
        _delaySec = 0;
    }
	
    /**
	 * Moves arm to specified angle with specified delay until start. Angle is absolute, 
	 * between 0 when the arm is facing straight down, angle increasing as the 
	 * arm rotates counterclockwise. 
	 * @param angleDeg, angle between 0 inclusive and 360 exclusive.
	 * @param delaySec, time to delay until start of movement in seconds
	 */
    public MoveArmToAngle(double angleDeg, double delaySec) {
        requires(Robot.arm);
        
        _targetAngleDeg = angleDeg;
        _delaySec = delaySec;
    }
    


	// Called just before this Command runs the first time
    protected void initialize() {
    		System.out.println("Move arm to angle started");
    		Timer.delay(_delaySec);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    		Robot.arm.moveToAngle(_targetAngleDeg);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    		boolean withinTolerance = (_targetAngleDeg - _endToleranceDeg) <= Robot.elevator.getHeight() 
        		&& Robot.elevator.getHeight() <= (_targetAngleDeg + _endToleranceDeg);
        return withinTolerance;
    }

    // Called once after isFinished returns true
    protected void end() {
    		System.out.println("end Move arm to angle");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    		end();
    }
}
