package com.example.songservice.dto;

import com.example.songservice.entity.Song;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongRequestDto {

	@NotEmpty(message = "name is required")
	private String name;

	@NotEmpty(message = "artist is required")
	private String artist;

	@NotEmpty(message = "album is required")
	private String album;

	@NotEmpty(message = "length is required")
	private String length;

	@NotEmpty(message = "resourceId is required")
	private String resourceId;

	@NotEmpty(message = "year is required")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
	private String year;

	public static Song toSongEntity(CreateSongRequestDto createSongRequest) {

		return new Song(createSongRequest.name,
						createSongRequest.artist,
						createSongRequest.album,
						createSongRequest.length,
						createSongRequest.resourceId,
						convertYearToLocalDate(createSongRequest.year));
	}

	private static LocalDate convertYearToLocalDate(String date){
		try {
			return LocalDate.parse(date);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format: " + date);
		}
	}
}
