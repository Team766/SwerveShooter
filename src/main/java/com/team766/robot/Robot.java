package com.team766.robot;

import com.team766.robot.mechanisms.*;

public class Robot {
	// Declare mechanisms here
	public static Shooter shooter;
	public static Drive drive;
	public static Gyro gyro;


	public static void robotInit() {
		// Initialize mechanisms here
		shooter = new Shooter();
		drive = new Drive();
		gyro = new Gyro();
		
	}
}
