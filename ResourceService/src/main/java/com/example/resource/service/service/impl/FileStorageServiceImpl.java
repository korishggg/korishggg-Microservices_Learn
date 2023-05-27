package com.example.resource.service.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.resource.service.exceptions.AwsInteractionException;
import com.example.resource.service.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

	private final AmazonS3 amazonS3Client;

	public FileStorageServiceImpl(AmazonS3 amazonS3Client) {
		this.amazonS3Client = amazonS3Client;
	}

	@Override
	public void createS3Bucket(String bucketName) {
		if(amazonS3Client.doesBucketExistV2(bucketName)) {
			LOGGER.info("Bucket name already in use. Try another name.");
		} else {
			amazonS3Client.createBucket(bucketName);
		}
	}

	@Override
	public String uploadFile(MultipartFile multipartFile, String bucketName, boolean publicObject) {
		String fileName = generateFileName(multipartFile);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		try {
			InputStream inputStream = multipartFile.getInputStream();
			var putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata).withCannedAcl(CannedAccessControlList.Private);
			amazonS3Client.putObject(putObjectRequest);
		} catch (Exception ex) {
			LOGGER.error("Some error has occurred = " + ex.getMessage());
			throw new AwsInteractionException(ex.getMessage());
		}
		return fileName;
	}

	private String generateFileName(MultipartFile multipartFile) {
		return System.nanoTime() + "-" + multipartFile.getOriginalFilename();
	}

	@Override
	public byte[] downloadFile(String fileName, String bucketName) {
		S3Object s3object = amazonS3Client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		try {
			return inputStream.readAllBytes();
		} catch (Exception e) {
			LOGGER.error("Some error has occurred = " + e.getMessage());
			throw new AwsInteractionException(e.getMessage());
		}
	}

	@Override
	public void deleteMultipleObjects(List<String> fileNames, String bucketName){
		DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName).withKeys(fileNames.toArray(new String[0]));
		amazonS3Client.deleteObjects(delObjectsRequests);
	}
}
