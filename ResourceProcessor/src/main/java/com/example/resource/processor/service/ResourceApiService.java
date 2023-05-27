package com.example.resource.processor.service;

import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;

public interface ResourceApiService {

	@Retryable(retryFor = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
	ResponseEntity<byte[]> getResourceById(Long resourceId);

}
