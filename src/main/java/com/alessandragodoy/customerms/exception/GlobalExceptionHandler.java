package com.alessandragodoy.customerms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * Handles specific and general exceptions and maps them to appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles all uncaught exceptions and returns a 500 Internal Server Error response.
	 *
	 * @param ex      the exception
	 * @param request the web request
	 * @return a ResponseEntity with a 500 status and a custom error response.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleDefaultException(Exception ex,
																	  WebRequest request) {

		CustomErrorResponse err = new CustomErrorResponse(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles all uncaught exceptions and returns a 404 Internal Server Error response.
	 *
	 * @param ex      the exception thrown
	 * @param request the web request during which the exception occurred
	 * @return ResponseEntity containing the error message and 404 status
	 */
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> handleCustomerNotFoundException(
			CustomerNotFoundException ex, WebRequest request) {

		CustomErrorResponse err = new CustomErrorResponse(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false)
		);

		return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles CustomerValidationException and returns a 400 Bad Request response for invalid
	 * parameters or malformed requests.
	 *
	 * @param ex      the CustomerValidationException that was thrown
	 * @param request the web request during which the exception occurred
	 * @return a ResponseEntity containing a 400 status and a custom error response.
	 */
	@ExceptionHandler(CustomerValidationException.class)
	public ResponseEntity<CustomErrorResponse> handleValidationException(
			CustomerValidationException ex, WebRequest request) {

		CustomErrorResponse err = new CustomErrorResponse(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false)
		);

		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles ExternalServiceException and returns a 503 Service Unavailable response
	 * when the external customer microservice is unreachable or fails.
	 *
	 * @param ex      the ExternalServiceException that was thrown
	 * @param request the web request during which the exception occurred
	 * @return a ResponseEntity containing a 503 status and a custom error response.
	 */
	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<CustomErrorResponse> handleExternalServiceException(
			ExternalServiceException ex, WebRequest request) {

		CustomErrorResponse err = new CustomErrorResponse(
				LocalDateTime.now(),
				ex.getMessage(),
				request.getDescription(false)
		);

		return new ResponseEntity<>(err, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
