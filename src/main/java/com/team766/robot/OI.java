package com.team766.robot;

import com.team766.framework.Procedure;
import com.team766.framework.Context;
import com.team766.hal.JoystickReader;
import com.team766.hal.RobotProvider;
import com.team766.logging.Category;
import com.team766.robot.procedures.*;
import edu.wpi.first.wpilibj.DriverStation;
import com.team766.robot.constants.InputConstants;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the code that allow control of the robot.
 */
public class OI extends Procedure {
	private JoystickReader rightJoystick;
	private JoystickReader leftJoystick;
	private JoystickReader joystick2;

	private double rightJoystickX = 0;
	private double RightJoystick_Y = 0;
	private double RightJoystick_Z = 0;
	private double RightJoystick_Theta = 0;
	private double leftJoystickX = 0;
	private double leftJoystickY = 0;
	private double LeftJoystick_Z = 0;
	private double LeftJoystick_Theta = 0;
	private boolean isCross = false;
	private static final double FINE_DRIVING_COEFFICIENT = 0.25;
	double turningValue = 0;
	boolean manualControl = true;

	public OI() {
		loggerCategory = Category.OPERATOR_INTERFACE;

		rightJoystick = RobotProvider.instance.getJoystick(0);
		leftJoystick = RobotProvider.instance.getJoystick(1);
		joystick2 = RobotProvider.instance.getJoystick(2);
	}

	public void run(final Context context) {
		context.takeOwnership(Robot.drive);
		while (true) {
			// wait for driver station data (and refresh it using the WPILib APIs)
			context.waitFor(() -> RobotProvider.instance.hasNewDriverStationData());
			RobotProvider.instance.refreshDriverStationData();

			leftJoystickX = leftJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT);
			leftJoystickY = leftJoystick.getAxis(InputConstants.AXIS_FORWARD_BACKWARD);
			rightJoystickX = rightJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT);
			// Add driver controls here - make sure to take/release ownership
			// of mechanisms when appropriate.

			if (leftJoystick.getButtonPressed(InputConstants.RESET_GYRO)) {
				Robot.gyro.resetGyro();
			}

			if (leftJoystick.getButtonPressed(InputConstants.RESET_POS)) {
				Robot.drive.resetCurrentPosition();
			}

			if (Math.abs(rightJoystick.getAxis(InputConstants.AXIS_FORWARD_BACKWARD)) > 0.05) {
				RightJoystick_Y = rightJoystick.getAxis(InputConstants.AXIS_FORWARD_BACKWARD);
			} else {
				RightJoystick_Y = 0;
			}
			if (Math.abs(rightJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT)) > 0.05) {
				rightJoystickX = rightJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT) / 2;
			} else {
				rightJoystickX = 0;	
			}
			if (Math.abs(rightJoystick.getAxis(InputConstants.AXIS_TWIST)) > 0.05) {
				RightJoystick_Theta = rightJoystick.getAxis(InputConstants.AXIS_TWIST);
			} else {
				RightJoystick_Theta = 0;
			}
			if (Math.abs(leftJoystick.getAxis(InputConstants.AXIS_FORWARD_BACKWARD)) > 0.05) {
				leftJoystickY = leftJoystick.getAxis(InputConstants.AXIS_FORWARD_BACKWARD);
			} else {
				leftJoystickY = 0;
			}
			if (Math.abs(leftJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT)) > 0.05) {
				leftJoystickX = leftJoystick.getAxis(InputConstants.AXIS_LEFT_RIGHT);
			} else {
				leftJoystickX = 0;
			}
			if (Math.abs(leftJoystick.getAxis(InputConstants.AXIS_TWIST)) > 0.05) {
				LeftJoystick_Theta = leftJoystick.getAxis(InputConstants.AXIS_TWIST);
			} else {
				LeftJoystick_Theta = 0;
			}

			// Moves the robot if there are joystick inputs
			if (!isCross && Math.abs(leftJoystickX) + Math.abs(leftJoystickY) + Math.abs(rightJoystickX) > 0) {
				context.takeOwnership(Robot.drive);
				// If a button is pressed, drive is just fine adjustment
				if (leftJoystick.getButton(InputConstants.FINE_DRIVING)) {
					Robot.drive.controlFieldOriented(Math.toRadians(Robot.gyro.getGyroYaw()), (-leftJoystickX * FINE_DRIVING_COEFFICIENT), (leftJoystickY * FINE_DRIVING_COEFFICIENT), (-rightJoystickX * FINE_DRIVING_COEFFICIENT));
				} else {
          // On deafault, controls the robot field oriented
          // Need negatives here, controls backwards otherwise (most likely specific to CLR)
					Robot.drive.controlFieldOriented(Math.toRadians(Robot.gyro.getGyroYaw()), (-leftJoystickX), (leftJoystickY), (-rightJoystickX));
				}
			} else if (!isCross) {
				Robot.drive.stopDrive();			
			} 

						// Sets the wheels to the cross position if the cross button is pressed
		if (rightJoystick.getButtonPressed(InputConstants.CROSS_WHEELS)) {
			if (!isCross) {
				context.startAsync(new setCross());
			}
				isCross = !isCross;
			}

		// Test shooting using 	
		if(joystick2.getButton(1)){
			//Robot.shooter.shoot(1, 1);
			try {
				Robot.shooter.testS(context);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//Robot.shooter.shoot(0,0);
		}

		//Button to press if you want to start a calibration session (so like a new distance)
		if(joystick2.getButtonPressed(2)){
			try {
				Robot.shooter.startNewCalibrationDistanceSession(context);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Button to press if the ball went long
		if(joystick2.getButtonPressed(3)){
			try {
				Robot.shooter.inputDataFromShot(false, true, context);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Button to press if the ball went short
		if(joystick2.getButtonPressed(4)){
			try {
				Robot.shooter.inputDataFromShot(false, false, context);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Button to press if the ball went in
		if(joystick2.getButtonPressed(5)){
			try {
				Robot.shooter.inputDataFromShot(true, false, context);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Send data to file
		if(joystick2.getButtonPressed(6)){
			Robot.shooter.resetCalibrationAndStoreDataInFine();
		}


		}

	}
}
