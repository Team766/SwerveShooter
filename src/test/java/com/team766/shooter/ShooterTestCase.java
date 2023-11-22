package com.team766.shooter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.team766.ViSIONbase.AutomaticShooterPowerCalibration;
import com.team766.framework.Scheduler;
import com.team766.hal.RobotProvider;
import com.team766.hal.mock.TestRobotProvider;

import edu.wpi.first.wpilibj.Filesystem;

public abstract class ShooterTestCase extends junit.framework.TestCase {

	ArrayList<Double> distancesToTest = new ArrayList<Double>();
	ArrayList<Double> powersThatWork = new ArrayList<Double>();

	AutomaticShooterPowerCalibration calibration;
	Scanner input;

	//@Rule
  	//public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {

		
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

	
	public void go() {
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
		calibration.reset();
	}

	private double goAgain(boolean a, boolean b){
		return calibration.thisHappenedWithShot(a, b);
	}

	@Test
	protected void isCorrect(){
		// since this is the first test, it should be this as its file name.

		go();

		String fileName = Filesystem.getDeployDirectory().getPath() + "/ShooterValueDataGenerated.dfa";
        File file = new File(fileName);
		
		try{
			input = new Scanner(file);
		} catch (FileNotFoundException e){
			fail("File was not created with correct name");
		}

		for(int i = 0; i < ((distancesToTest.size() * 2) - 1); i+=2){
			double distance = input.nextDouble();

			assertEquals("Distances tested need to equal each other", distancesToTest.get(i), distance);

			double power = input.nextDouble();
			System.out.println(power);
			assertEquals("Powers tested should be within a reasonable difference of what actually works", powersThatWork.get(i+1), power, 0.03); // see look how generoys
		}
		
	}

}
