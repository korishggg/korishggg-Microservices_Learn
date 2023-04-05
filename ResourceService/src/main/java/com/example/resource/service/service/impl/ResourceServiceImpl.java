package com.example.resource.service.service.impl;

import com.example.resource.service.dto.UploadResourceResponse;
import com.example.resource.service.entity.Resource;
import com.example.resource.service.exceptions.ResourceNotFoundException;
import com.example.resource.service.repository.ResourceRepository;
import com.example.resource.service.service.FileStorageService;
import com.example.resource.service.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	private final ResourceRepository resourceRepository;
	private final FileStorageService fileStorageService;

	public ResourceServiceImpl(ResourceRepository resourceRepository,
							   FileStorageService fileStorageService) {
		this.resourceRepository = resourceRepository;
		this.fileStorageService = fileStorageService;
	}

	@Override
	public UploadResourceResponse saveResource(MultipartFile multipartFile) {
		var resourceName = fileStorageService.uploadFile(multipartFile, bucketName, true);
		var resource = new Resource(resourceName, bucketName);
		var saved = resourceRepository.save(resource);
		LOGGER.info("resource with this id = " + saved.getId() + " is created");
		return new UploadResourceResponse(saved.getId());
	}

	@Override
	public void deleteByIds(Set<Long> ids) {
		List<Resource> resources = resourceRepository.findByIdIn(ids);
		if (!resources.isEmpty()) {
			List<String> fileNamesToDelete = resources.stream()
													  .map(Resource::getFileName)
													  .toList();
			this.fileStorageService.deleteMultipleObjects(fileNamesToDelete, bucketName);
			resourceRepository.deleteAll(resources);
		} else {
			LOGGER.info("resource with this ids = " + ids + " is not found");
			throw new ResourceNotFoundException("Resource is not Found");
		}
	}

	@Override
	public List<File> downloadFilesByResourceIdRange(Long[] range) {
		var resources = resourceRepository.findByIdBetween(range[0], range[1]);
		return resources.stream()
						.map(resource -> this.fileStorageService.downloadFile(resource.getFileName(), bucketName))
						.collect(Collectors.toList());
	}

	@Override
	public File downloadFileByResourceId(Long id) {
		var resource = resourceRepository.findById(id)
										 .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
		return this.fileStorageService.downloadFile(resource.getFileName(), bucketName);
	}
}
