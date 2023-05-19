package com.example.resource.processor.service;

import com.example.resource.processor.dto.CreateSongRequestDto;
import com.example.resource.processor.dto.CreateSongResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongServiceImpl implements SongApiService {

	@Value("${services.song-service.url}")
	private String SONG_SERVICE_URL;

	private final RestTemplate restTemplate;

	public SongServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public ResponseEntity<CreateSongResponseDto> createResource(CreateSongRequestDto createSongRequestDto) {
		// TODO handle EXCEPTIONS WITH SONG_SERVICE PROPERLY, SHOULD EFFECT CreateResourceListener
		return this.restTemplate.postForEntity(SONG_SERVICE_URL, createSongRequestDto, CreateSongResponseDto.class);
	}
}
