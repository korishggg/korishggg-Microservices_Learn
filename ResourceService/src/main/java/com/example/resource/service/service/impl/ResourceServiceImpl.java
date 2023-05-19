package com.example.resource.service.service.impl;

import com.example.resource.service.dto.FileDto;
import com.example.resource.service.dto.UploadResourceResponse;
import com.example.resource.service.entity.Resource;
import com.example.resource.service.exceptions.ResourceNotFoundException;
import com.example.resource.service.repository.ResourceRepository;
import com.example.resource.service.service.FileStorageService;
import com.example.resource.service.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	private final RabbitTemplate rabbitTemplate;
	private final Queue queue;
	private final ResourceRepository resourceRepository;
	private final FileStorageService fileStorageService;

	public ResourceServiceImpl(RabbitTemplate rabbitTemplate,
							   Queue queue,
							   ResourceRepository resourceRepository,
							   FileStorageService fileStorageService) {
		this.rabbitTemplate = rabbitTemplate;
		this.queue = queue;
		this.resourceRepository = resourceRepository;
		this.fileStorageService = fileStorageService;
	}

	@Override
	public UploadResourceResponse saveResource(MultipartFile multipartFile) {
		var resourceName = fileStorageService.uploadFile(multipartFile, bucketName, true);
		var resource = new Resource(resourceName, bucketName);
		var saved = resourceRepository.save(resource);
		// Send Message To RabbitMQ

		// TODO handle errors with send actions
		this.rabbitTemplate.convertAndSend(queue.getName(), saved.getId());

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
	public List<FileDto> downloadFilesByResourceIdRange(Long[] range) {
		var resources = resourceRepository.findByIdBetween(range[0], range[1]);
		return resources.stream()
						.map(resource -> {
							var fileBytes = this.fileStorageService.downloadFile(resource.getFileName(), bucketName);
							return this.mapFileBytesAndFileNameToFileDto(resource.getFileName(), fileBytes);
						})
						.collect(Collectors.toList());
	}

	@Override
	public FileDto downloadFileByResourceId(Long id) {
		var resource = resourceRepository.findById(id)
										 .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
		var fileBytes = this.fileStorageService.downloadFile(resource.getFileName(), bucketName);
		return this.mapFileBytesAndFileNameToFileDto(resource.getFileName(), fileBytes);
	}

	private FileDto mapFileBytesAndFileNameToFileDto(String fileName, byte[] fileBytes) {
		return new FileDto(fileName, fileBytes);
	}
}
