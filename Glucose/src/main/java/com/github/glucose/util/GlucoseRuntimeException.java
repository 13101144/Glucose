package com.github.glucose.util;

public class GlucoseRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1573662620270920138L;
	
	public GlucoseRuntimeException() {
		
	}
	
	public GlucoseRuntimeException(String message) {
		super(message);
	}
	
	public GlucoseRuntimeException(Throwable cause) {
		super(cause);
	}

	public GlucoseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
