package com.team766.robot.mechanisms;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.team766.framework.Mechanism;
import com.team766.hal.EncoderReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.MotorController;
import com.team766.library.RateLimiter;
import com.team766.hal.MotorController;
import com.team766.logging.Category;
//import edu.wpi.first.wpilibj.I2C.Port;
//import com.team766.hal.GyroReader;
//import com.kauailabs.navx.frc.*;
import com.ctre.phoenix.sensors.Pigeon2;

public class Gyro extends Mechanism {
	Pigeon2 g_gyro = new Pigeon2(0, "Swervavore");
	double[] gyroArray = new double[3];
	private RateLimiter l_loggingRate = new RateLimiter(0.05);
	public Gyro() {
		//loggerCategory = Category.GYRO;
	}
	public void resetGyro(){
		g_gyro.setYaw(0);
	}

	public void resetGyro180() {
		g_gyro.setYaw(180);
	}

	public void setGyro(double angleDeg) {
		g_gyro.setYaw(angleDeg);
	}
	
	public double getGyroPitch() {
		double angle = g_gyro.getPitch();
		return angle;
	}

	public double getGyroYaw() {
		double angle = -1* g_gyro.getYaw();
		return angle;
	}

	public double getGyroRoll() {
		double angle = g_gyro.getRoll();
		return angle;
	}

	@Override
	public void run() {
		 if (l_loggingRate.next()) {
			 gyroArray[0] = getGyroYaw();
			 gyroArray[1] = getGyroPitch();
			 gyroArray[2] = getGyroRoll();
			 SmartDashboard.putNumber("Yaw", gyroArray[0]);
			 SmartDashboard.putNumber("Pitch", gyroArray[1]);
			 SmartDashboard.putNumber("Roll", gyroArray[2]);
			 g_gyro.getYawPitchRoll(gyroArray);
		 }
	}

	/**
	 * @return combined pitch and roll values of gyro
	 */
	public double getAbsoluteTilt() {
		return Math.toDegrees(Math.acos(Math.cos(Math.toRadians(getGyroRoll()))*Math.cos(Math.toRadians(getGyroPitch()))));
	}
	
}