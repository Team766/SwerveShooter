package com.team766.robot.procedures;

import com.team766.framework.Context;
import com.team766.framework.Procedure;
import com.team766.robot.Robot;

public class ShootPower extends Procedure {
	private double power;
	public ShootPower(double power){
		this.power = power;
	}
	public void run(Context context){
		context.takeOwnership(Robot.shooter);
		Robot.shooter.shoot(power);
		context.waitForSeconds(5);
		Robot.shooter.shoot(0);
	}
}
