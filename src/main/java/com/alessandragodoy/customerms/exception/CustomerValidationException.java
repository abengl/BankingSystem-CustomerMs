package com.alessandragodoy.customerms.exception;

/**
 * Exception thrown when there is a validation error related to a customer.
 */
public class CustomerValidationException extends RuntimeException {
	public CustomerValidationException(String message) {
		super(message);
	}
}
