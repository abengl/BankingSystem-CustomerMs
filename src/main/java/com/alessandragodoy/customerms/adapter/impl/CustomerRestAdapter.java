package com.alessandragodoy.customerms.adapter.impl;

import com.alessandragodoy.customerms.adapter.CustomerServiceClient;
import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the CustomerAdapter interface for interacting with the Account Microservice
 * via REST calls.
 */
@Component
@RequiredArgsConstructor
public class CustomerRestAdapter implements CustomerAdapter {
	private final CustomerServiceClient customerServiceClient;

	@Override
	public boolean customerHasActiveAccounts(Integer customerId) {
		return customerServiceClient.customerHasActiveAccounts(customerId);
	}
}
