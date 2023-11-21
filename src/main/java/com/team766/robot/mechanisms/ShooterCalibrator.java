package com.team766.robot.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.ViSIONbase.*;

public class ShooterCalibrator extends Mechanism{
	
	AutomaticShooterPowerCalibration calibrator;

	public ShooterCalibrator(){
		calibrator = new AutomaticShooterPowerCalibration(3);
	}

	public void tryShot(){
		calibrator.shootAndCalculate();
	}

	public void giveShotResponse(boolean ballWentIn, boolean wasTooLong){
		calibrator.thisHappenedWithShot(ballWentIn, wasTooLong);
	}


	public void complete(){
		calibrator.reset();
	}


	
}
