package com.alessandragodoy.customerms.exception;

/**
 * Exception thrown when an external service is unreachable or fails.
 */
public class ExternalServiceException extends RuntimeException {
	public ExternalServiceException(String message) {
		super(message);
	}
}
