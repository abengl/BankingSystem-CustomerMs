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

		Customer existingCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
//		validateCustomerData(customer);
//		customer.setCustomerId(customerId);
		if (!customer.getEmail().isBlank()) {
			existingCustomer.setEmail(customer.getEmail());
		}
		if (!customer.getPhoneNumber().isBlank()) {
			existingCustomer.setPhoneNumber(customer.getPhoneNumber());
		}
		if (!customer.getAddress().isBlank()) {
			existingCustomer.setAddress(customer.getAddress());
		}

		return customerRepository.save(existingCustomer);
	}

	@Override
	public Customer createCustomer(Customer customer) throws Exception {

		return customerRepository.save(customer);

	}

	@Override
	public void deleteCustomerById(Integer customerId) throws Exception {

		Customer deactivatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));

		if (customerAdapter.customerHasAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}

		deactivatedCustomer.setActive(false);
		customerRepository.save(deactivatedCustomer);
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

}

