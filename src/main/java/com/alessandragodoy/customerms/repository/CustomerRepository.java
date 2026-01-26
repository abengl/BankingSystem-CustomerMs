package com.alessandragodoy.customerms.repository;

import com.alessandragodoy.customerms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Customer entities.
 * Provides methods for CRUD operations and custom queries.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * Retrieves a list of all active customers.
	 *
	 * @return {@code List<Customer>} a list of active customers.
	 */
	List<Customer> findAllByActiveTrue();

	/**
	 * Checks if an active customer exists with the given customer ID.
	 *
	 * @param customerId the ID of the customer to check
	 * @return {@code boolean} true if an active customer with the given ID exists,
	 * false otherwise.
	 */
	boolean existsByCustomerIdAndActiveTrue(Integer customerId);
}
