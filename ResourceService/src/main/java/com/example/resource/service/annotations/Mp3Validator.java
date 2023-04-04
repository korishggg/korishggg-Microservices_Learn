package com.example.resource.service.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class Mp3Validator implements ConstraintValidator<IsMp3, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null) {
			return false;
		}

		// Check if the file is actually an MP3 audio file
		String contentType = file.getContentType();
		return contentType != null && contentType.equals("audio/mpeg");
	}
}
