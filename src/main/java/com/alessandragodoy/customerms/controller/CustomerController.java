package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.service.ICustomerService;
import com.alessandragodoy.customerms.utility.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.alessandragodoy.customerms.utility.DTOMapper.convertToDTO;
import static com.alessandragodoy.customerms.utility.DTOMapper.convertToEntity;

/**
 * REST controller for managing customers.
 * Provides endpoints for CRUD operations on customers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Controller for Customer")
public class CustomerController {

	private final ICustomerService customerService;

	/**
	 * Retrieves a list of all customers.
	 *
	 * @return ResponseEntity containing the list of all customers.
	 */
	@Operation(summary = "Retrieve all customers", description = "Returns a list of CustomerDTO")
	@GetMapping
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws Exception {

		List<CustomerDTO> customers =
				customerService.getAllCustomers().stream().map(DTOMapper::convertToDTO).toList();
		return ResponseEntity.ok(customers);

	}

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be retrieved.
	 * @return ResponseEntity containing the customer data.
	 */
	@Operation(summary = "Retrieve a customer by its id", description = "Returns the customer found as a CustomerDTO")
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer customerId)
			throws Exception {

		Customer customer = customerService.getCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(customer));

	}

	/**
	 * Updates an existing customer by their ID.
	 *
	 * @param customerId  the ID of the customer to be updated.
	 * @param customerDTO the customer data transfer object containing the updated details of the customer.
	 * @return ResponseEntity containing the updated customer data.
	 */
	@Operation(summary = "Updates customer data", description = "Returns the customer with its data updated as a " +
			"CustomerDTO")
	@PutMapping("/{customerId}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer customerId,
													  @Valid @RequestBody CustomerDTO customerDTO)
			throws Exception {

		Customer updatedCustomer = customerService.updateCustomerById(customerId,
				convertToEntity(customerDTO));

		return ResponseEntity.ok(convertToDTO(updatedCustomer));

	}

	/**
	 * Creates a new customer.
	 *
	 * @param customerDTO the customer data transfer object containing the details of the customer to be created.
	 * @return ResponseEntity containing the created customer data.
	 */
	@Operation(summary = "Creates a customer with specific data", description = "Returns the customer created as a " +
			"CustomerDTO")
	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO)
			throws Exception {

		Customer customer = customerService.createCustomer(convertToEntity(customerDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(customer));

	}

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be deleted
	 * @return ResponseEntity&lt;Void&gt; indicating the result of the deletion operation
	 */
	@Operation(summary = "Deletes a customer by its id", description = "Returns the customer deleted as a CustomerDTO")
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) throws Exception {

		customerService.deleteCustomerById(customerId);
		return ResponseEntity.noContent().build();

	}

	/**
	 * Checks if a customer exists by their ID.
	 *
	 * @param customerId the ID of the customer to check
	 * @return true if the customer exists, false otherwise
	 */
	@Operation(summary = "Verify is a customer exists by its id", description = "Returns a boolean")
	@GetMapping("/exits/{customerId}")
	public boolean customerExists(@PathVariable Integer customerId) throws Exception {

		return customerService.customerExists(customerId);

	}

	/**
	 * Checks if a customer is active by their ID.
	 *
	 * @param customerId the ID of the customer to check
	 * @return true if the customer is active, false otherwise
	 */
	@Operation(summary = "Verify is a customer is active by its id", description = "Returns a " +
			"boolean")
	@GetMapping("/active/{customerId}")
	public boolean customerActive(@PathVariable Integer customerId) throws Exception {

		return customerService.customerIsActive(customerId);

	}
}
