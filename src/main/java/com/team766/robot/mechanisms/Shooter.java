package com.team766.robot.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.MotorController;
import com.team766.hal.RobotProvider;

public class Shooter extends Mechanism {
	
	private MotorController topShooterMotor;
	
	private MotorController leftShooterMotor;
	private MotorController rightShooterMotor;

	
	public Shooter(){
		topShooterMotor = RobotProvider.instance.getMotor("shooter.topMotor");

		leftShooterMotor = RobotProvider.instance.getMotor("shooter.leftMotor");
		rightShooterMotor = RobotProvider.instance.getMotor("shooter.rightMotor");
	}

	/*
	 * Premade method that turns all motors on a high speed
	 */
	public void shootFast(){
		topShooterMotor.set(0.8);

		leftShooterMotor.set(0.6);
		rightShooterMotor.set(-0.6);
	}
	

	public void shoot(double speedSides, double speedTop){
		topShooterMotor.set(speedTop);

		leftShooterMotor.set(speedSides);
		rightShooterMotor.set(-speedSides);
	}
}

