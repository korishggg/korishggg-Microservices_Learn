package com.example.resource.processor.service;

import com.example.resource.processor.dto.CreateSongRequestDto;
import com.example.resource.processor.dto.CreateSongResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;

public interface SongApiService {

	@Retryable(retryFor = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
	ResponseEntity<CreateSongResponseDto> createResource(CreateSongRequestDto createSongResponseDto);

}
