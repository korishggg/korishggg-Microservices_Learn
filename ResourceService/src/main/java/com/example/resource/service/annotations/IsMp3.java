package com.example.resource.service.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Mp3Validator.class)
public @interface IsMp3 {
	String message() default "Invalid MP3 file";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}