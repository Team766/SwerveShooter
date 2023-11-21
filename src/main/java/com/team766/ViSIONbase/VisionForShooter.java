package com.team766.ViSIONbase;
import org.photonvision.targeting.PhotonTrackedTarget;
import com.team766.ViSIONbase.*;
import com.team766.framework.*;
import edu.wpi.first.wpilibj.Filesystem;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

public class VisionForShooter {
	
	ArrayList<Double> distances = new ArrayList<Double>();
	ArrayList<Double> powers = new ArrayList<Double>();

	//.dfa format must be in the following format
	/*
	* DISTANCEVALUE
	* POWERVALUE
	* DISTANCEVALUE
	* POWERVALUE...
	*/
	String pathForData = Filesystem.getDeployDirectory().getPath() + "/CurrentValueData.dfa";
	Path path = Paths.get(pathForData);

	Scanner input;

	{
		try {
			input = new Scanner(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// I wonder if this is needed
	public VisionForShooter() throws AprilTagErrorCode{
		
		int currentLine = 1;

		while(input.hasNextDouble()){
			double val = input.nextDouble();

			if(currentLine % 2 == 1){
				distances.add(val);
				currentLine++;
			}else{
				powers.add(val);
				currentLine++;
			}
		}

		if(distances.size() != powers.size()) throw new AprilTagErrorCode ("The number of arguments corresponding to powers and distances did not equal each other", 8);
	}

	public double calculatePowerForDistance(double distance) throws AprilTagErrorCode{
		double closestOnMin = distances.get(0);
        double closestOnMax = distances.get(0);

        int indexMin = 0;
        int indexMax = 0;

		for(int i = 0; i < distances.size(); i++){
			if(Math.abs(distances.get(i) - distance) < 0.05){ // TODO: is this buffer okay?
				return powers.get(i);
			} 
		}

		for(int j = 1; j < distances.size(); j++){
			if(distances.get(j) < closestOnMin && Math.abs(distances.get(j) - distance) < Math.abs(closestOnMin - distance)){
				closestOnMin = distances.get(j);
				indexMin = j;
			}
			if(distances.get(j) > closestOnMax && Math.abs(distances.get(j) - distance) < Math.abs(closestOnMax - distance)){
				closestOnMax = distances.get(j);
				indexMax = j;
			}
		}

		double difference = closestOnMax - closestOnMin;
        double powerDifference = powers.get(indexMax) - powers.get(indexMin);

        double differenceFromMin = distance - closestOnMin;

        return (powers.get(indexMin) + (differenceFromMin / difference) * powerDifference);
	}
	
	
	public static CameraPlus findCameraThatHas(PhotonTrackedTarget target) throws AprilTagErrorCode{

		try {
			if (StaticCameras.camera1.getTagIdOfBestTarget() == target.getFiducialId()) {
				return StaticCameras.camera1;
			}
		} catch (Exception e1) {
			
		}
		
		try {
			if (StaticCameras.camera2.getTagIdOfBestTarget() == target.getFiducialId()) {
				return StaticCameras.camera2;
			}
		} catch (Exception e2) {
			
		}
		
		try {
			if (StaticCameras.camera3.getTagIdOfBestTarget() == target.getFiducialId()) {
				return StaticCameras.camera3;
			}
		} catch (Exception e3) {
			
		}
		
		// If none of the conditions are satisfied, return error code
		throw new AprilTagErrorCode("None of the cameras picked the AprilTag with Fiducial ID " + target.getFiducialId() + ".", 766);
		
	}

		// @overload
		public static CameraPlus findCameraThatHas(int targetID) throws AprilTagErrorCode{

		try {
			if (StaticCameras.camera1.getTagIdOfBestTarget() == targetID) {
				return StaticCameras.camera1;
			}
		} catch (Exception e1) {
			
		}
		
		try {
			if (StaticCameras.camera2.getTagIdOfBestTarget() == targetID) {
				return StaticCameras.camera2;
			}
		} catch (Exception e2) {
			
		}
		
		try {
			if (StaticCameras.camera3.getTagIdOfBestTarget() == targetID) {
				return StaticCameras.camera3;
			}
		} catch (Exception e3) {
			
		}
		
		// If none of the conditions are satisfied, return error code
		throw new AprilTagErrorCode("None of the cameras picked the AprilTag with Fiducial ID " + targetID + ".", 766);
		
	}
		
}

