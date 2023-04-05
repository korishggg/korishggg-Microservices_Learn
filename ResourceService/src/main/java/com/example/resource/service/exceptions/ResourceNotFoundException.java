package com.example.resource.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private final String message;

	public ResourceNotFoundException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
