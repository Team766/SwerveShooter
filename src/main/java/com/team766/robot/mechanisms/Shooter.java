package com.team766.robot.mechanisms;

import java.util.ArrayList;
import javax.swing.text.AbstractDocument.Content;
import com.team766.ViSIONbase.AutomaticShooterPowerCalibration;
import com.team766.framework.Context;
import com.team766.framework.Mechanism;
import com.team766.hal.MotorController;
import com.team766.hal.RobotProvider;
import com.team766.robot.procedures.ShootPower;

public class Shooter extends Mechanism {
	
	private MotorController topShooterMotor;
	
	private MotorController leftShooterMotor;
	private MotorController rightShooterMotor;




	static AutomaticShooterPowerCalibration calibration;

	private double lastSpeed = 0;
	public Shooter(){
		calibration = new AutomaticShooterPowerCalibration(1);
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

	public void shoot(double speed){
		checkContextOwnership();
		topShooterMotor.set(speed);
		lastSpeed = speed;
		//TODO: Check to see if this is the correct direction
		leftShooterMotor.set(1);
		rightShooterMotor.set(-1);
	}

	public double getLastSpeed(){
		return lastSpeed;
	}

	public void testS(Context context) throws InterruptedException{
		shootForFiveSeconds(0.8, context);

		shoot(0,0);
		System.out.println("iyrueajil;k");
	}

	public void startNewCalibrationDistanceSession(Context context) throws InterruptedException{
		context.yield();
		//Context contexta = new Context();
		//context.startAsync(shootForFiveSeconds(ab, context));
		
		double ab = calibration.shootAndCalculate();
		context.startAsync(new ShootPower(ab));
		log("ab: " + ab);
	}

	public void inputDataFromShot(boolean wentIn, boolean wasLong, Context context) throws InterruptedException{
		double ab = calibration.thisHappenedWithShot(wentIn, wasLong);
		context.startAsync(new ShootPower(ab));
	}

	public void resetCalibrationAndStoreDataInFine(){
		calibration.reset();
	}

	public void shootForFiveSeconds(double power, Context context) throws InterruptedException{
		shoot(power);
		context.yield();
		context.waitForSeconds(5);
	}


}

