package com.alessandragodoy.customerms.service;

import com.alessandragodoy.customerms.exception.AccountsNotFoundException;
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
	 * Retrieves all customers.
	 *
	 * @return a list of CustomerDTO objects
	 */
	List<Customer> getAllActiveCustomers() throws Exception;

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return the CustomerDTO if found
	 * @throws AccountsNotFoundException if the customer is not found
	 */
	Customer getCustomerById(Integer customerId) throws Exception;

	/**
	 * Updates a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @param customer the customer data to update
	 * @return the updated CustomerDTO if successful
	 * @throws AccountsNotFoundException if the customer is not found
	 */
	Customer updateCustomerById(Integer customerId, Customer customer) throws Exception;

	/**
	 * Creates a new customer.
	 *
	 * @param customer the customer data to create
	 * @return the created CustomerDTO
	 */
	Customer createCustomer(Customer customer) throws Exception;

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @throws AccountsNotFoundException   if the customer is not found
	 * @throws CustomerValidationException if the customer has accounts and cannot be deleted
	 * @throws ExternalServiceException    if there is an error connecting to the account service
	 */
	void deleteCustomerById(Integer customerId) throws Exception;

	/**
	 * Checks if a customer exists by their ID.
	 *
	 * @param customerId the ID of the customer
	 * @return true if the customer exists, false otherwise
	 */
	boolean customerExists(Integer customerId) throws Exception;

	boolean customerIsActive(Integer customerId) throws Exception;
}
