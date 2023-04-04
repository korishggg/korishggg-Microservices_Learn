package com.example.songservice.dto;

import com.example.songservice.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {

	private Long id;
	private String name;
	private String artist;
	private String album;
	private String length;
	private String resourceId;
	private LocalDate year;

	public static SongDto entityToDto(Song song) {
		return new SongDto(song.getId(),
						   song.getName(),
						   song.getArtist(),
						   song.getAlbum(),
						   song.getLength(),
						   song.getResourceId(),
						   song.getYear());
	}
}
