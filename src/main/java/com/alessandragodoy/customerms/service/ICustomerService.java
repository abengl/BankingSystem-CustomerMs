package com.alessandragodoy.customerms.service;

import com.alessandragodoy.customerms.exception.CustomerNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.exception.ExternalServiceException;
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
	List<Customer> getAllActiveCustomers() throws Exception;

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return {@code Customer} if found
	 * @throws CustomerNotFoundException if the customer is not found
	 */
	Customer getCustomerById(Integer customerId) throws Exception;

	/**
	 * Creates a new customer.
	 *
	 * @param customer the customer data to create
	 * @return {@code Customer} created
	 * @throws Exception if an error occurs during creation
	 */
	Customer createCustomer(Customer customer) throws Exception;

	/**
	 * Updates a customer information by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @param customer   the customer data to update
	 * @return {@code Customer} updated if successful
	 * @throws CustomerNotFoundException if the customer is not found
	 */
	Customer updateCustomerById(Integer customerId, Customer customer) throws Exception;

	/**
	 * Activates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to activate
	 * @return {@code Customer} activated
	 * @throws CustomerNotFoundException if the customer is not found
	 */
	Customer activateCustomerById(Integer customerId) throws Exception;

	/**
	 * Deactivates a customer by their ID.
	 *
	 * @param customerId the ID of the customer to deactivate
	 * @return {@code Customer} deactivated
	 * @throws CustomerNotFoundException   if the customer is not found
	 * @throws CustomerValidationException if the customer has active accounts and cannot be
	 * deactivated
	 * @throws ExternalServiceException    if there is an error connecting to the account service
	 */
	Customer deactivateCustomerById(Integer customerId) throws Exception;

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @throws CustomerNotFoundException   if the customer is not found
	 * @throws CustomerValidationException if the customer has accounts and cannot be deleted
	 * @throws ExternalServiceException    if there is an error connecting to the account service
	 */
	void deleteCustomerById(Integer customerId) throws Exception;

	/**
	 * Checks if a customer exists by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return {@code boolean}true if the customer exists, false otherwise
	 * @throws Exception if an error occurs during the check
	 */
	boolean customerExists(Integer customerId);

	/**
	 * Checks if a customer is active by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return {@code boolean} true if the customer is active, false otherwise
	 * @throws Exception if an error occurs during the check
	 */
	boolean customerIsActive(Integer customerId);
}
