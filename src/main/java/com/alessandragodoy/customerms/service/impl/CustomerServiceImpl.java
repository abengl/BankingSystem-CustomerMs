package com.alessandragodoy.customerms.service.impl;

import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import com.alessandragodoy.customerms.exception.AccountsNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import com.alessandragodoy.customerms.service.ICustomerService;
import com.alessandragodoy.customerms.utility.CustomerValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
	public List<Customer> getAllCustomers() throws Exception {

		return customerRepository.findAll();
	}

	@Override
	public Customer getCustomerById(Integer customerId) throws Exception {

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
	}

	@Override
	public Customer updateCustomerById(Integer customerId, Customer customer) throws Exception {

		validateCustomerData(customer);
		customer.setCustomerId(customerId);

		customerRepository.findById(customerId)
				.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		return customerRepository.save(customer);
	}

	@Override
	public Customer createCustomer(Customer customer) throws Exception {

		validateCustomerData(customer);

		return customerRepository.save(customer);
	}

	@Override
	public void deleteCustomerById(Integer customerId) throws Exception{

		Customer updatedCustomer =
				customerRepository.findById(customerId).orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		if (customerAdapter.customerHasAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}
		updatedCustomer.setActive(false);
		customerRepository.save(updatedCustomer);
	}

	@Override
	public boolean customerExists(Integer customerId) throws Exception {
		return customerRepository.existsById(customerId);
	}

	@Override
	public boolean customerIsActive(Integer customerId) throws Exception {
		return customerRepository.findById(customerId)
				.map(Customer::isActive)
				.orElse(false);
	}

	/* Helper methods */
	private void validateCustomerData(Customer customer) {
		CustomerValidationUtils.checkRequiredFields(
				customer.getFirstName(),
				customer.getLastName(),
				customer.getDocumentNumber(),
				customer.getEmail()
		);
		CustomerValidationUtils.checkDniFormat(customer.getDocumentNumber());
		CustomerValidationUtils.checkEmailFormat(customer.getEmail());
		checkDniUniqueness(customer.getDocumentNumber(), customer.getCustomerId());

	}

	private void checkDniUniqueness(String dni, Integer customerId) {
		Optional<Customer> customerOptional = customerRepository.findByDocumentNumber(dni);
		customerOptional.ifPresent(customer -> {
			if (!customer.getCustomerId().equals(customerId)) {
				throw new CustomerValidationException("DNI number is already registered.");
			}
		});
	}
}

