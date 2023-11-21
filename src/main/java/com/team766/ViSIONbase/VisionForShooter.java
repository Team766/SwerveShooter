package com.team766.ViSIONbase;
import org.photonvision.targeting.PhotonTrackedTarget;
import com.team766.ViSIONbase.*;
import com.team766.framework.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
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
	String pathForData = "src/main/java/com/team766/ViSIONbase/ShooterValueData.dfa";
	Path path = Paths.get(pathForData);

	Scanner input;

	{
		try {
			input = new Scanner(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public VisionForShooter(double differentiationPerDistance) throws AprilTagErrorCode{
		
		int currentLine = 1;

		while(input.hasNextDouble()){
			double val = input.nextDouble();

			if(currentLine % 2 == 1){
				if(currentLine > 1 && ( Math.abs(distances.get((distances.size() - 1)) - val)) != differentiationPerDistance) throw new AprilTagErrorCode ("The distance at line " + currentLine + " was not consistant with what the common difference is supposed to be.", 254);
				distances.add(val);
				currentLine++;
			}else{
				powers.add(val);
				currentLine++;
			}
		}

		if(distances.size() != powers.size()) throw new AprilTagErrorCode ("The number of arguments corresponding to powers and distances did not equal each other", 8);
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

		// overload
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

