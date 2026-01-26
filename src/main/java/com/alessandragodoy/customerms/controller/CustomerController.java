package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.controller.dto.UpdateCustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.service.ICustomerService;
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
	 * Retrieves a list of all active customers.
	 *
	 * @return {@code ResponseEntity<List<CustomerDTO>>} containing the list of all customers.
	 * @throws Exception if an error occurs during the retrieval process
	 */
	@Operation(summary = "Retrieve all active customers", description = "Returns a list of " +
			"CustomerDTO")
	@GetMapping
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws Exception {

		List<CustomerDTO> customers = customerService.getAllActiveCustomers().stream()
				.map(customer -> convertToDTO(customer, CustomerDTO.class)).toList();

		return ResponseEntity.ok(customers);

	}

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be retrieved.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the customer data.
	 * @throws Exception if an error occurs during the retrieval process
	 */
	@Operation(summary = "Retrieve a customer by its id", description = "Returns the customer " +
			"found as a CustomerDTO")
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer customerId)
			throws Exception {

		Customer customer = customerService.getCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(customer, CustomerDTO.class));

	}

	/**
	 * Creates a new customer.
	 *
	 * @param customerDTO the customer data transfer object containing the details of the customer
	 *                    to be created.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the created customer data.
	 * @throws Exception if an error occurs during the creation process
	 */
	@Operation(summary = "Creates a customer with specific data", description = "Returns the " +
			"customer created as a CustomerDTO")
	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO)
			throws Exception {

		Customer customer =
				customerService.createCustomer(convertToEntity(customerDTO, Customer.class));

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(convertToDTO(customer, CustomerDTO.class));

	}

	/**
	 * Updates an existing customer information by their ID.
	 *
	 * @param customerId        the ID of the customer to be updated.
	 * @param updateCustomerDTO the customer data transfer object with updated details.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the updated customer data.
	 * @throws Exception if an error occurs during the update process
	 */
	@Operation(summary = "Updates customer information", description = "Returns the customer " +
			"with its data updated as a CustomerDTO")
	@PatchMapping("/update/{customerId}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer customerId,
													  @Valid @RequestBody UpdateCustomerDTO updateCustomerDTO)
			throws Exception {

		Customer updatedCustomer = customerService.updateCustomerById(customerId,
				convertToEntity(updateCustomerDTO, Customer.class));

		return ResponseEntity.ok(convertToDTO(updatedCustomer, CustomerDTO.class));

	}

	/**
	 * Activates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be activated
	 * @return {@code ResponseEntity<CustomerDTO>} containing the activated customer data
	 * @throws Exception if an error occurs during the activation process
	 */
	@Operation(summary = "Activates a customer by its id", description =
			"Returns the customer activated as a CustomerDTO")
	@PatchMapping("/activate/{customerId}")
	public ResponseEntity<CustomerDTO> activateCustomer(@PathVariable Integer customerId)
			throws Exception {

		Customer activatedCustomer = customerService.activateCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(activatedCustomer, CustomerDTO.class));

	}

	/**
	 * Deactivates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be deactivated
	 * @return {@code ResponseEntity<CustomerDTO>} containing the deactivated customer data
	 * @throws Exception if an error occurs during the deactivation process
	 */
	@Operation(summary = "Deactivates a customer by its id", description =
			"Returns the customer deactivated as a CustomerDTO")
	@PatchMapping("/deactivate/{customerId}")
	public ResponseEntity<CustomerDTO> deactivateCustomer(@PathVariable Integer customerId)
			throws Exception {

		Customer deactivatedCustomer = customerService.deactivateCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(deactivatedCustomer, CustomerDTO.class));

	}

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be deleted
	 * @return {@code ResponseEntity<Void>} empty response after successful operation
	 * @throws Exception if an error occurs during the deleting process
	 */
	@Operation(summary = "Deletes a customer by its id", description =
			"Returns empty response after successful operation")
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) throws Exception {

		customerService.deleteCustomerById(customerId);

		return ResponseEntity.noContent().build();

	}

	/**
	 * Checks if a customer exists by their ID.
	 *
	 * @param customerId the ID of the customer to check
	 * @return {@code boolean} true if the customer exists, false otherwise
	 * @throws Exception if an error occurs during the existence check
	 */
	@Operation(summary = "Verify is a customer exists by its id", description = "Returns a " +
			"boolean")
	@GetMapping("/exists/{customerId}")
	public boolean customerExists(@PathVariable Integer customerId) throws Exception {

		return customerService.customerExists(customerId);

	}

	/**
	 * Checks if a customer is active by their ID.
	 *
	 * @param customerId the ID of the customer to check
	 * @return {@code boolean} true if the customer is active, false otherwise
	 * @throws Exception if an error occurs during the active status check
	 */
	@Operation(summary = "Verify is a customer is active by its id", description = "Returns a " +
			"boolean")
	@GetMapping("/active/{customerId}")
	public boolean customerActive(@PathVariable Integer customerId) throws Exception {

		return customerService.customerIsActive(customerId);

	}
}
