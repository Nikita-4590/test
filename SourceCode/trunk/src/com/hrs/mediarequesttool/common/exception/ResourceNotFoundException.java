package com.hrs.mediarequesttool.common.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 293491416765826207L;

	private final Logger logger;

	public ResourceNotFoundException() {
		super();

		logger = null;
	}
	
	public ResourceNotFoundException(String message) {
		this(message, ResourceNotFoundException.class);
	}
	
	public ResourceNotFoundException(String message, Class<?> source) {
		super(message);
		
		logger = Logger.getLogger(source);
		
		this.logException();
		
	}
	
	public ResourceNotFoundException(Throwable cause) {
		this(cause, ResourceNotFoundException.class);
		
		this.logException();
	}

	public ResourceNotFoundException(Throwable cause, Class<?> source) {
		super(cause);

		logger = Logger.getLogger(source);
		
		logger.error(this.getMessage(), this.getCause());
	}
	
	public ResourceNotFoundException(String message, Throwable cause) {
		this(message, cause, ResourceNotFoundException.class);
		
		this.logException();
	}
	
	public ResourceNotFoundException(String message, Throwable cause, Class<?> source) {
		super(message, cause);
		
		logger = Logger.getLogger(source);
		
		this.logException();
	}
	
	private void logException() {
		if (logger != null) {
			logger.error(this.getMessage(), this.getCause());
		}
	}

}
