package com.example.resource.service.api;

import com.example.resource.service.annotations.IsMp3;
import com.example.resource.service.dto.UploadResourceResponse;
import com.example.resource.service.service.ResourceService;
import com.example.resource.service.utils.HttpValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/resources")
@Validated
public class ResourceController {

	private final ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@PostMapping
	public ResponseEntity<UploadResourceResponse> uploadResource(@RequestParam("file") @IsMp3 MultipartFile file) {
		var response = resourceService.saveResource(file);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<?> getByIds(@RequestHeader(value = "Range", required = false) String rangeHeader) {
		if (rangeHeader != null) {
			Long[] range = HttpValidationUtils.parseRangeHeaders(rangeHeader);
			var result = resourceService.downloadFilesByResourceIdRange(range);
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
								 .body(result);
		}
		return ResponseEntity.badRequest()
							 .body("Please specify Range Header by this pattern X-X");
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		var fileByResourceId = this.resourceService.downloadFileByResourceId(id);
		return ResponseEntity.ok(fileByResourceId);
	}

	@DeleteMapping
	public ResponseEntity<Set<Long>> deleteByIds(@RequestParam("ids") Set<Long> ids) {
		this.resourceService.deleteByIds(ids);
		return ResponseEntity.ok(ids);
	}
}
