package com.team766.ViSIONbase;
import org.photonvision.targeting.PhotonTrackedTarget;
import com.team766.ViSIONbase.*;
import com.team766.framework.*;

public class VisionForShooter {
	

	public VisionForShooter(){

	}
	
	
	public CameraPlus findCameraThatHas(PhotonTrackedTarget target) throws AprilTagErrorCode{

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
		
		// If none of the conditions are satisfied, return null
		throw new AprilTagErrorCode("None of the cameras picked the AprilTag with Fiducial ID " + target.getFiducialId() + ".", 502);
		
	}
		
}

