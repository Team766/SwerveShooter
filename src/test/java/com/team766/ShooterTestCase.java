package com.team766;

import java.util.ArrayList;

import com.team766.ViSIONbase.AutomaticShooterPowerCalibration;
import com.team766.config.ConfigFileReader;
import com.team766.framework.Scheduler;
import com.team766.hal.RobotProvider;
import com.team766.hal.mock.MockJoystick;
import com.team766.hal.mock.TestRobotProvider;

public abstract class ShooterTestCase extends junit.framework.TestCase {

	ArrayList<Double> distancesToTest = new ArrayList<Double>();
	ArrayList<Double> powersThatWork = new ArrayList<Double>();

	AutomaticShooterPowerCalibration calibration;
	@Override
	protected void setUp() throws Exception {
		calibration = new AutomaticShooterPowerCalibration(3);

		//The powers we get back should model y = 0.15(2.5)^x
		distancesToTest.add(0.5);
		distancesToTest.add(1.0);
		distancesToTest.add(1.5);
		distancesToTest.add(2.0);

		powersThatWork.add(.2371);
		powersThatWork.add(.3750);
		powersThatWork.add(.5929);
		powersThatWork.add(.9375);



		RobotProvider.instance = new TestRobotProvider();

		Scheduler.getInstance().reset();
	}

	protected void go() {
		for(int i = 0; i<distancesToTest.size(); i++){
			double power = calibration.shootAndCalculate(distancesToTest.get(i));

			//generous error margin
			if(Math.abs(power - powersThatWork.get(i)) < 0.03){
				calibration.thisHappenedWithShot(true, false);
			}else{
				double lastPower = power;

				while(Math.abs(lastPower - powersThatWork.get(i)) < 0.03){
					if(lastPower > powersThatWork.get(i)){
						lastPower = goAgain(false, true);
					}else{
						lastPower = goAgain(false, false);
					}
				}
				
			}
		}
	}

	private double goAgain(boolean a, boolean b){
		return calibration.thisHappenedWithShot(a, b);
	}

}
