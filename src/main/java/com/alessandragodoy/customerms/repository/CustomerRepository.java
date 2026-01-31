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
	 * Checks if a customer with the given document number exists.
	 *
	 * @param documentNumber the document number to check.
	 * @return {@code boolean} true if a customer with the document number exists, false otherwise.
	 */
	boolean existsByDocumentNumber(String documentNumber);
}
