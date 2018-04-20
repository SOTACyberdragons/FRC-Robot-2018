package org.usfirst.frc.team5700.robot.subsystems;

import org.usfirst.frc.team5700.robot.Constants;
import org.usfirst.frc.team5700.robot.Instrum;
import org.usfirst.frc.team5700.robot.Robot;
import org.usfirst.frc.team5700.robot.commands.MoveArmWithJoystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem {
	
	//Motor Controller
	private WPI_TalonSRX talon;
	
	//preferences
	Preferences prefs;

	//Constants
	public final static double REDUCTION_TO_ENCODER = 114.55; 
	public final static double TICKS_PER_DEG = (Constants.VERSA_ENCODER_TPR * REDUCTION_TO_ENCODER) / 360;
	public final static double ENCODER_MAX_SPEED = 33000; //ticks per 100 ms
	
	public double wCubeMaxNominalOutput; //Maximum nominal output, when arm is horizontal to ground
	public double noCubeMaxNominalOutput;

	@SuppressWarnings("unused")
	private double dangerOfCollisionHeight = 14;
	private double collisionAngle = 50;
	
	public ArmCollisionBounds withCubeBounds = new ArmCollisionBounds(28.5, 40, 3);
	public ArmCollisionBounds noCubeBounds = new ArmCollisionBounds(14, 40, 5);
	
	public Arm() {
		
		super();
		
		talon = new WPI_TalonSRX(2);
		talon.setName("Arm", "Talon");
		/* first choose the sensor */
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
		setEncoder180();
		talon.setSensorPhase(true);
		talon.setInverted(true);
	
		/* Set relevant frame periods to be at least as fast as periodic rate */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);
	
		/* set the peak and nominal outputs */
		talon.configNominalOutputForward(0, Constants.TIMEOUT_MS);
		talon.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
		talon.configPeakOutputForward(1, Constants.TIMEOUT_MS);
		talon.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);
	
		/* set closed loop gains in slot 0 - see documentation */
		talon.selectProfileSlot(Constants.SLOT_IDX, Constants.PID_LOOP_IDX);
		talon.config_kF(0, Constants.TALON_MAX_OUTPUT/ENCODER_MAX_SPEED, Constants.TIMEOUT_MS);
		talon.config_kP(0, 0.1, Constants.TIMEOUT_MS);
		talon.config_kI(0, 0, Constants.TIMEOUT_MS);
		talon.config_kD(0, 0, Constants.TIMEOUT_MS);
		
		/* set acceleration and cruise velocity - see documentation */
		talon.configMotionCruiseVelocity(25000 , Constants.TIMEOUT_MS);
		talon.configMotionAcceleration(30000, Constants.TIMEOUT_MS);
	}
	
	public void moveArmWithJoystick(double stickValue) {
		StringBuilder sb = new StringBuilder();
		
		setTalon(stickValue + getFeedForward());
			
		/* instrumentation */
		Instrum.Process(talon, sb);
    }
	
	public TalonSRX getTalon() {
		return talon;
	}
	
	public void setEncoder180() {
		talon.setSelectedSensorPosition((int)(180 * TICKS_PER_DEG), Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
	}
	
	public double getRawEncoderTicks() {
		return talon.getSelectedSensorPosition(0);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new MoveArmWithJoystick());
    }
    
    public void log() {
    		talon.getMotorOutputVoltage();
    }
    
    public double getFeedForward() {
    		prefs = Preferences.getInstance();
		wCubeMaxNominalOutput = prefs.getDouble("armWCubeOut", 0.15);
		noCubeMaxNominalOutput = prefs.getDouble("armNoCubeOut", 0.075);
		
    		//Feed Forward Logic
    		return Math.sin(Math.toRadians(getRawAngle())) * (Robot.grabber.hasCube() ? wCubeMaxNominalOutput : noCubeMaxNominalOutput);
    }
    
    /**
     * Sets TalonSRX to output value
     * @param output, as percent input (-1 through 1)
     */
    private void setTalon(double output) {
    		talon.set(ControlMode.PercentOutput, output);
    }
    
//    /**
//     * @deprecated
//     * @param angle in deg, 0 - 359
//     * 
//     * @return null if coordinate system is ok, starting angle in degrees if
//     * coordinate system must be restored to original.
//     */
//    public double moveToAngle(double targetAngleDeg) {
//    		double newTargetAngleDeg = (Double) null;
//    		double delta = getAngle() - targetAngleDeg;
//    		
//    		if (Math.abs(delta) > 180) {
//    			zeroEncoder();
//    			double newAngleToMove = delta / Math.abs(delta) * (360 - Math.abs(delta));
//    			_talon.set(ControlMode.MotionMagic, newAngleToMove * ticksPerDeg);
//    			newTargetAngleDeg = newAngleToMove;
//    		} else {
//    			_talon.set(ControlMode.MotionMagic, targetAngleDeg * ticksPerDeg);
//    		}
//    		
//    		/* append more signals to print when in speed mode. */
//    		StringBuilder sb = new StringBuilder();
//    		sb.append("\terror:");
//    		sb.append(_talon.getClosedLoopError(Constants.kPIDLoopIdx));
//    		sb.append("\ttarget:");
//    		sb.append(targetAngleDeg);
//    		
//    		Instrum.Process(_talon, sb);
//    		
//    		return newTargetAngleDeg;
//    }
//    
//    public void restoreCoordinateSystem(double targetAngleDeg, double newTargetAngleDeg) {
//    		Double restoredCurrentAngle = ((getAngle() - newTargetAngleDeg) + targetAngleDeg)* ticksPerDeg;
//    		_talon.setSelectedSensorPosition(restoredCurrentAngle.intValue(), Constants.kPIDLoopIdx, Constants.kTimeoutMs);
//    }
    
    public void moveToAngle(double targetAngleDeg) {
    	
	    	double currentAngle = getRawAngle();
	    	double closestAngle = getClosestAngle(currentAngle, targetAngleDeg);
	   		ArmCollisionBounds bounds = Robot.grabber.hasCube() ? withCubeBounds: noCubeBounds;
	   		
	   		if (Robot.elevator.getHeight() < bounds.heightIn
	   				&& Math.abs(targetAngleDeg) < bounds.outsideAngle
	   				&& !((Math.abs(targetAngleDeg) < bounds.insideAngle))) {
	    		
	    		if ((currentAngle - closestAngle) < 0) {
	    			closestAngle = getClosestAngle(currentAngle, -collisionAngle);
	    		} else {
	    			closestAngle = getClosestAngle(currentAngle, collisionAngle);
	    		}
	    	}
	    
	   		talon.set(ControlMode.MotionMagic, closestAngle * TICKS_PER_DEG);
    }
    
    /**
     * @return raw angle in degrees, not reseting at 360
     */
    public double getRawAngle() {
    		return talon.getSelectedSensorPosition(0) / TICKS_PER_DEG;
    }
    
    /**
     * @return angle in degrees between -180 and 180
     */
    public double get180NormalizedAngle() {
    		double angle = getRawAngle() % 360;
    		
    		if (angle < -180) {
    			angle += 360;
    		} else if (angle > 180) {
    			angle -= 360;
    		}
   
    		return angle;
    }
    
    private double getClosestAngle(double currentAngle, double targetAngle) {
    		
    		int delta = (int) Math.floor(currentAngle/360);
	    	double normalizedCurrentAngle = currentAngle % 360;
	    	normalizedCurrentAngle += normalizedCurrentAngle < 0 ? 360 : 0;
	    
	    	if (normalizedCurrentAngle < (targetAngle - 180)) {
	    		delta += -1;
	    	} else if (normalizedCurrentAngle > (targetAngle + 180)) {
	    		delta += 1;
	    	} 
	    	
	    	return targetAngle + 360 * delta;	
	}
    
    public class ArmCollisionBounds {
    	
    	public double heightIn, outsideAngle, insideAngle;
    	
    	public ArmCollisionBounds(double heightIn, double outsideAngle, double insideAngle) {
    		this.heightIn = heightIn;
    		this.outsideAngle = outsideAngle;
    		this.insideAngle = insideAngle;
		}
    }
}

