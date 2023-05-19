package com.example.resource.processor.listeners;

import com.example.resource.processor.dto.CreateSongRequestDto;
import com.example.resource.processor.dto.CreateSongResponseDto;
import com.example.resource.processor.service.ResourceApiService;
import com.example.resource.processor.service.SongApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@RabbitListener(queues = "create-resource", id = "create-resource-listener")
public class CreateResourceListener {

	private static final Logger logger = LogManager.getLogger(CreateResourceListener.class.toString());

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");;

	private final ResourceApiService resourceApiService;
	private final SongApiService songApiService;

	public CreateResourceListener(ResourceApiService resourceApiService, SongApiService songApiService) {
		this.resourceApiService = resourceApiService;
		this.songApiService = songApiService;
	}

	@RabbitHandler
	public void receiver(Long resourceId) {
		logger.info("Create resource listener invoked - Consuming resourceId message : " +  resourceId);

		ResponseEntity<byte[]> resourceServiceResponse = resourceApiService.getResourceById(resourceId);

		logger.info("Received : Resource with id =" +  resourceId);

		InputStream stream = new ByteArrayInputStream(Objects.requireNonNull(resourceServiceResponse.getBody()));

		Metadata metadata = new Metadata();
		ContentHandler handler = new DefaultHandler();
		Mp3Parser parser = new Mp3Parser();
		ParseContext parseCtx = new ParseContext();
		try {
			parser.parse(stream, handler, metadata, parseCtx);
		} catch (SAXException | TikaException | IOException e) {
			e.printStackTrace();
//			TODO later add logic to remove this file
		}

		String title = this.getMetadataOrDefaultValue(metadata,"dc:title", "title " + resourceId);
		String artist = this.getMetadataOrDefaultValue(metadata,"xmpDM:artist", "artist " + resourceId);
		String album = this.getMetadataOrDefaultValue(metadata,"xmpDM:album", "album " + resourceId);
		String length = this.getMetadataOrDefaultValue(metadata,"xmpDM:duration", "100");
		String localDate = this.getLocalDateMetadataOrDefaultValue(metadata,"xmpDM:releaseDate", LocalDate.now().format(DATE_TIME_FORMATTER));

		CreateSongRequestDto createSongRequestDto = new CreateSongRequestDto(title, artist, album, length, resourceId.toString(), localDate);

		ResponseEntity<CreateSongResponseDto> songServiceResponse = songApiService.createResource(createSongRequestDto);

		logger.info("Processed : Song with id =" +  Objects.requireNonNull(songServiceResponse.getBody()).getId());
	}

	private String getMetadataOrDefaultValue(Metadata metadata, String metadataField, String defaultValue) {
		var metadataValue = metadata.get(metadataField);
		if (metadataValue == null || metadataValue.isBlank()) {
			return defaultValue;
		} else {
			return metadataValue;
		}
	}

	private String getLocalDateMetadataOrDefaultValue(Metadata metadata, String metadataField, String defaultValue) {
		var metadataValue = metadata.get(metadataField);
		if (metadataValue == null || metadataValue.isBlank()) {
			return defaultValue;
		} else {
			return LocalDate.parse(metadataValue).format(DATE_TIME_FORMATTER);
		}
	}
}