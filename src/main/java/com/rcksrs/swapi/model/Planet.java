package com.rcksrs.swapi.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document
public class Planet {
	
	@Id
	private String id;
	
	@Indexed
	@NotBlank
	private String name;
	
	@NotBlank
	private String climate;
	
	@NotBlank
	private String terrain;
	
	private Set<Film> films = new HashSet<>();	
	
	public Planet(String name, String climate, String terrain) {
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}
	
	public Planet(String id, String name, String climate, String terrain) {
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}
	
	@Transient
	public Integer getFilmCount() {
		return films.size();
	}

}
