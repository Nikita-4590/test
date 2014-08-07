package com.hrs.mediarequesttool.common.exception;

import org.apache.log4j.Logger;

public class GenericException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2034940596310338557L;

	private final Logger logger;

	public GenericException() {
		super();

		logger = null;
	}

	public GenericException(String message) {
		this(message, GenericException.class);
	}

	public GenericException(String message, Class<?> source) {
		super(message);

		logger = Logger.getLogger(source);

		this.logException();
	}

	public GenericException(Throwable cause) {
		this(cause, GenericException.class);

		this.logException();
	}

	public GenericException(Throwable cause, Class<?> source) {
		super(cause);

		logger = Logger.getLogger(source);

		this.logException();
	}

	public GenericException(String message, Throwable cause) {
		this(message, cause, GenericException.class);

		this.logException();
	}

	public GenericException(String message, Throwable cause, Class<?> source) {
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
