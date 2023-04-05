package com.example.songservice.service.impl;

import com.example.songservice.dto.CreateSongRequestDto;
import com.example.songservice.dto.CreateSongResponseDto;
import com.example.songservice.dto.SongDto;
import com.example.songservice.entity.Song;
import com.example.songservice.exceptions.AlreadyExistsException;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import com.example.songservice.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class SongServiceImpl implements SongService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SongServiceImpl.class);
	private final SongRepository songRepository;

	public SongServiceImpl(SongRepository songRepository) {
		this.songRepository = songRepository;
	}

	@Override
	public CreateSongResponseDto save(CreateSongRequestDto createSong) {
		validateCreation(createSong);
		var songEntity = CreateSongRequestDto.toSongEntity(createSong);
		var saved = songRepository.save(songEntity);
		LOGGER.info("Song with this id = " + saved.getId() + " is already created");
		return new CreateSongResponseDto(saved.getId());
	}

	private void validateCreation(CreateSongRequestDto createSong) {
		Optional<Song> optionalSong = this.songRepository.findByNameAndAlbumAndArtist(createSong.getName(), createSong.getAlbum(), createSong.getArtist());
		if (optionalSong.isPresent()) {
			LOGGER.info("Song with this name = "+ createSong.getName() + " album = " + createSong.getAlbum() + " artist = " + createSong.getArtist() + " is already exists");
			throw new AlreadyExistsException("This Song Is Already Exists");
		}
	}

	@Override
	public SongDto findById(Long id) {
		var song = getById(id);
		return SongDto.entityToDto(song);
	}

	@Override
	@Transactional
	public void deleteByIds(Set<Long> ids) {
		this.songRepository.deleteByIdIn(ids);
	}

	private Song getById(Long id) {
		return songRepository.findById(id)
							 .orElseThrow(() -> new ResourceNotFoundException("Resource not Found"));
	}
}
