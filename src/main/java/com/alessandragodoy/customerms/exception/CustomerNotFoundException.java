package com.alessandragodoy.customerms.exception;

/**
 * Exception thrown when accounts are not found.
 */
public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String message) {

		super(message);
	}
}
