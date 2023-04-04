package com.example.songservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "song")

public class Song {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String artist;
	private String album;
	private String length;
	private String resourceId;
	private LocalDate year;

	public Song() {
	}

	public Song(String name, String artist, String album, String length, String resourceId, LocalDate year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.length = length;
		this.resourceId = resourceId;
		this.year = year;
	}

	public Song(Long id, String name, String artist, String album, String length, String resourceId, LocalDate year) {
		this(name, artist, album, length, resourceId, year);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public LocalDate getYear() {
		return year;
	}

	public void setYear(LocalDate year) {
		this.year = year;
	}
}
