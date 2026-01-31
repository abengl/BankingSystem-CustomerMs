package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.api.CustomerApi;
import com.alessandragodoy.customerms.dto.CustomerDTO;
import com.alessandragodoy.customerms.dto.UpdateCustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alessandragodoy.customerms.utility.DTOMapper.convertToDTO;
import static com.alessandragodoy.customerms.utility.DTOMapper.convertToEntity;

/**
 * REST controller for managing customers.
 * Provides endpoints for CRUD operations on customers.
 */
@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

	private final ICustomerService customerService;

	/**
	 * Activates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be activated
	 * @return {@code ResponseEntity<CustomerDTO>} containing the activated customer data
	 */
	@Override
	public ResponseEntity<CustomerDTO> activateCustomer(@PathVariable Integer customerId) {

		Customer activatedCustomer = customerService.activateCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(activatedCustomer, CustomerDTO.class));

	}

	/**
	 * Creates a new customer.
	 *
	 * @param customerDTO the customer data transfer object containing the details of the customer
	 *                    to be created.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the created customer data.
	 */
	@Override
	public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {

		Customer customer =
				customerService.createCustomer(convertToEntity(customerDTO, Customer.class));

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(convertToDTO(customer, CustomerDTO.class));

	}

	/**
	 * Deactivates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be deactivated
	 * @return {@code ResponseEntity<CustomerDTO>} containing the deactivated customer data
	 */
	@Override
	public ResponseEntity<CustomerDTO> deactivateCustomer(@PathVariable Integer customerId) {

		Customer deactivatedCustomer = customerService.deactivateCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(deactivatedCustomer, CustomerDTO.class));

	}

	/*
	@Override
	public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {

		customerService.deleteCustomerById(customerId);

		return ResponseEntity.noContent().build();

	}*/

	/**
	 * Retrieves a list of all active customers.
	 *
	 * @return {@code ResponseEntity<List<CustomerDTO>>} containing the list of all customers.
	 */
	@Override
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

		List<CustomerDTO> customers = customerService.getAllActiveCustomers().stream()
				.map(customer -> convertToDTO(customer, CustomerDTO.class)).toList();

		return ResponseEntity.ok(customers);

	}

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer to be retrieved.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the customer data.
	 */
	@Override
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer customerId) {

		Customer customer = customerService.getCustomerById(customerId);

		return ResponseEntity.ok(convertToDTO(customer, CustomerDTO.class));

	}

	/**
	 * Updates an existing customer information by their ID.
	 *
	 * @param customerId        the ID of the customer to be updated.
	 * @param updateCustomerDTO the customer data transfer object with updated details.
	 * @return {@code ResponseEntity<CustomerDTO>} containing the updated customer data.
	 */
	@Override
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer customerId,
													  @Valid @RequestBody UpdateCustomerDTO updateCustomerDTO) {

		Customer updatedCustomer = customerService.updateCustomerById(customerId,
				convertToEntity(updateCustomerDTO, Customer.class));

		return ResponseEntity.ok(convertToDTO(updatedCustomer, CustomerDTO.class));

	}

}
