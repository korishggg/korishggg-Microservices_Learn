package com.example.songservice.api;

import com.example.songservice.dto.CreateSongRequestDto;
import com.example.songservice.dto.CreateSongResponseDto;
import com.example.songservice.dto.SongDto;
import com.example.songservice.service.impl.SongServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/songs")
public class SongController {

	private final SongServiceImpl songService;

	public SongController(SongServiceImpl songService) {
		this.songService = songService;
	}

	@PostMapping
	public ResponseEntity<CreateSongResponseDto> create(@Valid @RequestBody CreateSongRequestDto createSong) {
		CreateSongResponseDto createdSong = this.songService.save(createSong);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(createdSong);
	}

	@GetMapping
	@RequestMapping("/{id}")
	public ResponseEntity<SongDto> getById(@PathVariable Long id) {
		SongDto song = this.songService.findById(id);
		return ResponseEntity.ok(song);
	}

	@DeleteMapping
	public ResponseEntity<Set<Long>> deleteByIds(@RequestParam("ids") Set<Long> ids) {
		this.songService.deleteByIds(ids);
		return ResponseEntity.ok(ids);
	}

}
