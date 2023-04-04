package com.example.resource.service;

import com.example.resource.service.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServiceApplication implements CommandLineRunner {

	private final FileStorageService fileStorageService;

	private final String bucketName;

	public ResourceServiceApplication(FileStorageService fileStorageService,
									  @Value("${aws.s3.bucketName}") String bucketName) {
		this.fileStorageService = fileStorageService;
		this.bucketName = bucketName;
	}

	public static void main(String[] args) {
		SpringApplication.run(ResourceServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.fileStorageService.createS3Bucket(bucketName);
	}
}
