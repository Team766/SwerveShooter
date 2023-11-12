package com.team766.framework;

public class AprilTagErrorCode extends Exception {
	
	// Error Code Variable, you can get the error code in the accessor method
	private int errorCode;

	public AprilTagErrorCode(String e, int errorCode){
		super(e);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
}
