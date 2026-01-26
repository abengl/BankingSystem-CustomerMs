package com.alessandragodoy.customerms.adapter;

import com.alessandragodoy.customerms.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Singleton bean for communicating with the Account microservice.
 * Thread-safe because it is stateless and its dependencies are immutable.
 */
@Component
public class CustomerServiceClient {
	private final RestTemplate restTemplate;
	private final String accountMsUrl;

	public CustomerServiceClient(RestTemplate restTemplate, @Value("${account.ms.url}") String accountMsUrl) {
		this.restTemplate = restTemplate;
		this.accountMsUrl = accountMsUrl;
	}

	public boolean customerHasActiveAccounts(Integer customerId) {
		String url = UriComponentsBuilder.fromHttpUrl(accountMsUrl).pathSegment(customerId.toString()).toUriString();
		try {
			ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
			return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody());
		} catch (ResourceAccessException e) {
			throw new ExternalServiceException("Unable to connect to the customer service." + e.getMessage());
		}
	}
}

