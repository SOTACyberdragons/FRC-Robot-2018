package org.usfirst.frc.team5700.robot.commands;

import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.utils.LinearAccelerationFilter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DrivePastDistance extends Command {

	private double distanceIn;
	private double speed;
	private boolean stop;
	private boolean dropCubeAtEnd;

	public DrivePastDistance(double distanceIn, double speed, boolean stop, boolean dropCubeAtEnd) {
        requires(Robot.drivetrain);
        
        this.distanceIn = distanceIn;
        this.speed = speed;
        this.stop = stop;
        this.dropCubeAtEnd = dropCubeAtEnd;
    }
	
	public DrivePastDistance(double distanceIn, boolean stop) {
        requires(Robot.drivetrain);
        this.distanceIn = distanceIn;
        this.stop = stop;
        speed = 0.5;
    }

    protected void initialize() {
    		//logs
    		System.out.println("Initializing DrivePastDistance Command");
    		System.out.println("Using preset distance");
    		
    		System.out.println("First Distance: " + distanceIn);
	    	System.out.println("driveSpeed: " + speed);
	    	
	    	Robot.drivetrain.resetSensors();
    }

    protected void execute() {
    		Robot.drivetrain.drive(-speed, 0);
    }

    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.getDistance()) > distanceIn;
    }

    protected void end() {
    		if (dropCubeAtEnd) {
    			Robot.dropCube = true;
    		}
    		
    		Robot.drivetrain.resetSensors();
    		
//    		if (stop) {
//    			while (Robot.drivetrain.getAverageEncoderRate() > 5) {
//    				Robot.drivetrain.drive(0, 0);
//    				Timer.delay(0.02);
//			}
    		Robot.drivetrain.drive(0, 0);
    		
    		System.out.println("DrivePastDistance Command Finished");
    }

    protected void interrupted() {
    		end();
    }
}
