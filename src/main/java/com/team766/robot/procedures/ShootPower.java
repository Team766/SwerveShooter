package com.team766.robot.procedures;

import com.team766.framework.Context;
import com.team766.framework.Procedure;
import com.team766.robot.Robot;

public class ShootPower extends Procedure {
	// private variable power to store amount of power that should be shot. This is set in the constructor.
	private double power;
	public ShootPower(double power){
		this.power = power;
		this.power = Math.min(this.power, 1);
		this.power = Math.max(this.power, -1);
	}
	public void run(Context context){
		context.takeOwnership(Robot.shooter);
		Robot.shooter.shoot(power);
		context.waitForSeconds(5);
		Robot.shooter.shoot(0);
	}
}
