package org.usfirst.frc.team5700.utils;

import edu.wpi.first.wpilibj.Joystick;

public class ButtonLocator {
	
	public int buttonNum;
	public Joystick joystick;
	
	public ButtonLocator(int buttonNum, Joystick joystick) {
		
		System.out.println("In ButtonLocator");
		this.buttonNum = buttonNum;
		this.joystick = joystick;
		System.out.println("Button: " + buttonNum + ", joystick: " + joystick.getPort());
	}

}

