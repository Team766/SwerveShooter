package com.team766.shooter;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.*;

import com.team766.ViSIONbase.AutomaticShooterPowerCalibration;
import com.team766.framework.Scheduler;
import com.team766.hal.RobotProvider;
import com.team766.hal.mock.TestRobotProvider;

import edu.wpi.first.wpilibj.Filesystem;


public class ShooterTestCase {

	static ArrayList<Double> distancesToTest = new ArrayList<Double>();
	static ArrayList<Double> powersThatWork = new ArrayList<Double>();

	static AutomaticShooterPowerCalibration calibration;
	Scanner input;

	//@Rule
  	//public ExpectedException exception = ExpectedException.none();
	
	@BeforeAll
	public static void setUp() throws Exception {

		
		calibration = new AutomaticShooterPowerCalibration(1);

		//The powers we get back should model y = 0.15(2.5)^x
		distancesToTest.add(0.5);
		distancesToTest.add(1.0);
		distancesToTest.add(1.5);
		distancesToTest.add(2.0);

		powersThatWork.add(0.2371);
		powersThatWork.add(0.3750);
		powersThatWork.add(0.5925);
		powersThatWork.add(0.9375);



		RobotProvider.instance = new TestRobotProvider();

		Scheduler.getInstance().reset();
	}

	
	public void go() {

		for(int i = 0; i < distancesToTest.size(); i++){
			double power = calibration.shootAndCalculate(distancesToTest.get(i).doubleValue());

			//generous error margin
			if(Math.abs(power - powersThatWork.get(i).doubleValue()) < 0.03){
				calibration.thisHappenedWithShot(true, false);
			}else{
				double lastPower = power;

				while(Math.abs(lastPower - powersThatWork.get(i).doubleValue()) > 0.03){
					if(lastPower > powersThatWork.get(i).doubleValue()){
						lastPower = goAgain(false, true);
					}else{
						lastPower = goAgain(false, false);
					}
				}
				calibration.thisHappenedWithShot(true, false);
			}
		}
		calibration.reset();
	}

	private double goAgain(boolean a, boolean b){
		return calibration.thisHappenedWithShot(a, b);
	}

	@Test
	protected void isCorrect(){
		// since this is the first test, it should be this as its file name.

		go();

		// String fileName = Filesystem.getDeployDirectory().getPath() + "/ShooterValueDataGenerated.dfa";
        // File file = new File(fileName);
		
		// try{
		// 	input = new Scanner(file);
		// } catch (FileNotFoundException e){
		// 	fail("File was not created with correct name");
		// }

		ArrayList<Double> dist = calibration.getDistances();
		ArrayList<Double> pow = calibration.getPowers();

		for(int i = 0; i < distancesToTest.size(); i++){

			assertEquals("Distances tested need to equal each other", (double)distancesToTest.get(i), (double)dist.get(i),0);
			assertEquals("Powers tested should be within a reasonable difference of what actually works", (double)powersThatWork.get(i), (double)pow.get(i), 0.03); // see look how generoys
		}
		
	}

}
