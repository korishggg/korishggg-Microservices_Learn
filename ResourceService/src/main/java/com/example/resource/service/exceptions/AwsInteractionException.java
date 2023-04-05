package com.example.resource.service.exceptions;

public class AwsInteractionException extends RuntimeException {

	private final String message;

	public AwsInteractionException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
