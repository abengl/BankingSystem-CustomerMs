package com.alessandragodoy.customerms.adapter;

/**
 * Adapter interface for interacting with the Account Microservice.
 * Provides a method to check if a customer has accounts.
 *
 * <p>This interface follows the Adapter design pattern, which allows the
 * Customer Service to interact with the Account Microservice without being
 * tightly coupled to its implementation.</p>
 */
public interface CustomerAdapter {
	boolean customerHasActiveAccounts(Integer customerId);
}
