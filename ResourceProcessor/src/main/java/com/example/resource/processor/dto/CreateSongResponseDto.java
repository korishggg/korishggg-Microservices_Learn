package com.example.resource.processor.dto;

public class CreateSongResponseDto {

	private Long id;

	public CreateSongResponseDto() {
	}

	public CreateSongResponseDto(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
