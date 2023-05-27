package com.example.resource.service.service;

import com.example.resource.service.dto.FileDto;
import com.example.resource.service.dto.UploadResourceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ResourceService {
	UploadResourceResponse saveResource(MultipartFile file);
	void deleteByIds(Set<Long> ids);
	List<FileDto> downloadFilesByResourceIdRange(Long[] range);
	FileDto downloadFileByResourceId(Long id);
}
