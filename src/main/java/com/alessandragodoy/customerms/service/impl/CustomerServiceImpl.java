package com.alessandragodoy.customerms.service.impl;

import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import com.alessandragodoy.customerms.exception.AccountsNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import com.alessandragodoy.customerms.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the CustomerService interface.
 * Provides methods for managing customers.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerAdapter customerAdapter;

	/* Customer MS CRUD methods */
	@Override
	public List<Customer> getAllActiveCustomers() throws Exception {

		return customerRepository.findAllByActiveTrue();
	}

	@Override
	public Customer getCustomerById(Integer customerId) throws Exception {

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
	}

	@Override
	public Customer createCustomer(Customer customer) throws Exception {

		return customerRepository.save(customer);

	}

	@Override
	public Customer updateCustomerById(Integer customerId, Customer customer) throws Exception {

		Customer updatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		if (!customer.getEmail().isBlank()) {
			updatedCustomer.setEmail(customer.getEmail());
		}
		if (!customer.getPhoneNumber().isBlank()) {
			updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
		}
		if (!customer.getAddress().isBlank()) {
			updatedCustomer.setAddress(customer.getAddress());
		}

		return customerRepository.save(updatedCustomer);
	}

	@Override
	public Customer activateCustomerById(Integer customerId) {

		Customer activatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		activatedCustomer.setActive(true);

		return customerRepository.save(activatedCustomer);
	}

	@Override
	public Customer deactivateCustomerById(Integer customerId) {

		Customer deactivatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		if (customerAdapter.customerHasActiveAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}

		deactivatedCustomer.setActive(false);

		return customerRepository.save(deactivatedCustomer);
	}

	@Override
	public void deleteCustomerById(Integer customerId) throws Exception {

		Customer deletedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		if (customerAdapter.customerHasActiveAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}

		customerRepository.delete(deletedCustomer);
	}

	@Override
	public boolean customerExists(Integer customerId) throws Exception {

		return customerRepository.existsById(customerId);
	}

	@Override
	public boolean customerIsActive(Integer customerId) throws Exception {

		return customerRepository.existsByCustomerIdAndActiveTrue(customerId);
	}

}

