package com.example.resource.processor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceApiServiceImpl implements ResourceApiService {

	@Value("${services.resource-service.url}")
	private String RESOURCE_SERVICE_URL;

	private final RestTemplate restTemplate;

	public ResourceApiServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public ResponseEntity<byte[]> getResourceById(Long resourceId) {
		// TODO handle EXCEPTIONS WITH RESOURCE_SERVICE PROPERLY, SHOULD EFFECT CreateResourceListener
		return this.restTemplate.getForEntity(RESOURCE_SERVICE_URL + "/" + resourceId, byte[].class);
	}
}
