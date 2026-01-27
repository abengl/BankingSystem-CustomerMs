package com.alessandragodoy.customerms.adapter;

import com.alessandragodoy.customerms.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Singleton bean for communicating with the Account microservice.
 * Thread-safe because it is stateless and its dependencies are immutable.
 */
@Component
@RequiredArgsConstructor
public class AccountServiceClient {

	private final RestTemplate restTemplate;

	@Value("${account.ms.url}")
	private String accountMsBaseUrl;


	public boolean customerHasActiveAccounts(Integer customerId) {

		String url = UriComponentsBuilder.fromHttpUrl(accountMsBaseUrl)
				.pathSegment(customerId.toString()).toUriString();

		try {
			ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
			return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(
					response.getBody());
		} catch (RestClientException ex) {
			throw new ExternalServiceException(
					"Unable to connect to the customer service." + ex.getMessage());
		}
	}
}

