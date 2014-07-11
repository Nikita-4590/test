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

	public ResourceNotFoundException(Throwable cause, Class<?> source) {
		super(cause);

		logger = Logger.getLogger(source);
		
		logger.error(this.getMessage(), this.getCause());
	}

}
