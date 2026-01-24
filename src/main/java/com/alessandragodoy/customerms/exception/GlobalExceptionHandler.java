package com.alessandragodoy.customerms.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Handles specific and general exceptions and maps them to appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * Handles AccountsNotFoundException and returns a 404 Not Found response for
	 * unregistered accounts.
	 *
	 * @param e the AccountsNotFoundException thrown
	 * @return ResponseEntity containing the error message and 404 status
	 */
	@ExceptionHandler(AccountsNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFoundException(AccountsNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	/**
	 * Handles CustomerValidationException and returns a 400 Bad Request response for
	 * invalid parameters or invalid requests.
	 *
	 * @param e the CustomerValidationException thrown
	 * @return ResponseEntity containing the error message and 400 status
	 */
	@ExceptionHandler(CustomerValidationException.class)
	public ResponseEntity<String> handleValidationException(CustomerValidationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	/**
	 * Handles ExternalServiceException and returns a 503 Service Unavailable response
	 * when the external customer microservice is unreachable or fails.
	 *
	 * @param e the ExternalServiceException thrown
	 * @return ResponseEntity containing the error message and 503 status
	 */
	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<String> handleExternalServiceException(ExternalServiceException e) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {

		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

}
