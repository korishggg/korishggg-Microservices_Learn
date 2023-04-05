package com.example.songservice.repository;

import com.example.songservice.entity.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface SongRepository extends CrudRepository<Song, Long> {
	Optional<Song> findByNameAndAlbumAndArtist(String name, String album, String artist);
	void deleteByIdIn(Set<Long> id);
}
