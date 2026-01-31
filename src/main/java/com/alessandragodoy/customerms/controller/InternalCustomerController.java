package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.api.internal.InternalCustomerApi;
import com.alessandragodoy.customerms.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for internal customer operations.
 * Provides endpoints for verifying customer existence and status.
 */
@RestController
@RequiredArgsConstructor
public class InternalCustomerController implements InternalCustomerApi {

	private final ICustomerService customerService;

	/**
	 * Validates a customer by their unique identifier.
	 *
	 * @param customerId The unique identifier of the customer to validate (required)
	 * @return {@code ResponseEntity<CustomerValidationResponseDTO>} containing the validation result
	 */
	@Override
	public ResponseEntity<CustomerValidationResponseDTO> validateCustomer(
			@PathVariable Integer customerId) {

		CustomerValidationResponseDTO validation =
				customerService.validateCustomer(customerId);

		return ResponseEntity.ok(validation);
	}

}
