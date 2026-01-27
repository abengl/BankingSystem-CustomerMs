package com.alessandragodoy.customerms.service;

import com.alessandragodoy.customerms.controller.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.model.Customer;

import java.util.List;

/**
 * Service interface for managing customers.
 * Provides methods for CRUD operations on customers.
 */
public interface ICustomerService {
	/**
	 * Retrieves all active customers.
	 *
	 * @return {@code List<Customer>} a list of active customers
	 */
	List<Customer> getAllActiveCustomers();

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return {@code Customer} if found
	 */
	Customer getCustomerById(Integer customerId);

	/**
	 * Creates a new customer.
	 *
	 * @param customer the customer data to create
	 * @return {@code Customer} created
	 */
	Customer createCustomer(Customer customer);

	/**
	 * Updates a customer information by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @param customer   the customer data to update
	 * @return {@code Customer} updated if successful
	 */
	Customer updateCustomerById(Integer customerId, Customer customer);

	/**
	 * Activates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to activate
	 * @return {@code Customer} activated
	 */
	Customer activateCustomerById(Integer customerId);

	/**
	 * Deactivates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to deactivate
	 * @return {@code Customer} deactivated
	 */
	Customer deactivateCustomerById(Integer customerId);

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 */
	void deleteCustomerById(Integer customerId);

	/**
	 * Validates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to validate
	 * @return {@code CustomerValidationResponseDTO} containing validation result
	 */

	CustomerValidationResponseDTO validateCustomer(Integer customerId);
}
