package com.example.resource.service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
	void createS3Bucket(String bucketName);
	String uploadFile(MultipartFile multipartFile, String bucketName, boolean publicObject);
	byte[] downloadFile(String fileName, String bucketName);
	void deleteMultipleObjects(List<String> fileNames, String bucketName);
}
