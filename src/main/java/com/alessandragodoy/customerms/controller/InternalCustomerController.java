package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.controller.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.service.ICustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for internal customer operations.
 * Provides endpoints for verifying customer existence and status.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/customers")
@Tag(name = "Internal Customers", description = "Controller for microservices' Customer " +
		"operations")
public class InternalCustomerController {

	private final ICustomerService customerService;

	@GetMapping("/validate-customer/{customerId}")
	public ResponseEntity<CustomerValidationResponseDTO> validateCustomer(
			@PathVariable Integer customerId) {

		CustomerValidationResponseDTO validation =
				customerService.validateCustomer(customerId);

		return ResponseEntity.ok(validation);
	}

}
