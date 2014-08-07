package com.hrs.mediarequesttool.common.exception;

import org.apache.log4j.Logger;

public class DDosException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger;

	public DDosException() {
		super();

		logger = null;
	}

	public DDosException(String message) {
		this(message, DDosException.class);
	}

	public DDosException(String message, Class<?> source) {
		super(message);

		logger = Logger.getLogger(source);
		
		this.logException();
	}
	
	public DDosException(Throwable cause, Class<?> source) {
		super(cause);

		logger = Logger.getLogger(source);

		logger.error(this.getMessage(), this.getCause());
	}
	
	private void logException() {
		if (logger != null) {
			logger.error(this.getMessage(), this.getCause());
		}
	}
	
}
