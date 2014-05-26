package com.hrs.mediarequesttool.common.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4684360431231425066L;
	private final Logger logger;

	public BadRequestException() {
		super();

		logger = null;
	}

	public BadRequestException(Throwable cause, Class<?> source) {
		super(cause);

		logger = Logger.getLogger(source);

		logger.error(this.getMessage(), this.getCause());
	}

}