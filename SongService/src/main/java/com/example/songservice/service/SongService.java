package com.example.songservice.service;

import com.example.songservice.dto.CreateSongRequestDto;
import com.example.songservice.dto.CreateSongResponseDto;
import com.example.songservice.dto.SongDto;

import java.util.Set;

public interface SongService {
	CreateSongResponseDto save(CreateSongRequestDto createSong);
	SongDto findById(Long id);
	void deleteByIds(Set<Long> ids);
}
