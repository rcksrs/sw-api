package com.rcksrs.swapi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rcksrs.swapi.exception.DuplicatedEntityException;
import com.rcksrs.swapi.exception.EntityNotFoundException;
import com.rcksrs.swapi.model.Film;
import com.rcksrs.swapi.model.Planet;
import com.rcksrs.swapi.model.dto.PlanetDTO;
import com.rcksrs.swapi.model.dto.ResponseDTO;
import com.rcksrs.swapi.repository.PlanetRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlanetService {
	private PlanetRepository planetRepository;
	private RestTemplate restTemplate;
	
	public Planet findById(String planetId) {
		var planet = planetRepository.findById(planetId).orElseThrow(() -> new EntityNotFoundException());
		return planet;		
	}
	
	public Planet findByName(String name) {
		var planet = planetRepository.findByNameIgnoreCase(name).orElseThrow(() -> new EntityNotFoundException());
		return planet;
	}
	
	public List<Planet> filterByName(String name) {
		return planetRepository.findByNameContainingIgnoreCase(name);	
	}
	
	public List<Planet> findAll() {
		return planetRepository.findAll(Sort.by("name"));
	}
	
	public Page<Planet> findAll(Pageable pageable) {
		return planetRepository.findAll(pageable);
	}
	
	public Planet save(Planet planet) {
		if(planet.getId() != null || planetRepository.findByNameIgnoreCase(planet.getName()).isPresent())
			throw new DuplicatedEntityException();
		
		Set<Film> films = findFilmsByPlanetName(planet.getName());
		planet.setFilms(films);
		return planetRepository.save(planet);
	}
	
	public Planet update(Planet planet) {
		if(planet.getId() == null || planetRepository.findById(planet.getId()).isEmpty()) throw new EntityNotFoundException();
		
		Set<Film> films = findFilmsByPlanetName(planet.getName());
		planet.setFilms(films);
		return planetRepository.save(planet);
	}
	
	public void delete(String planetId) {
		var planet = planetRepository.findById(planetId).orElseThrow(() -> new EntityNotFoundException());
		planetRepository.delete(planet);		
	}
	
	private Set<Film> findFilmsByPlanetName(String name) {
		try {
			var response = restTemplate.getForObject("https://swapi.dev/api/planets/?name=" + name, ResponseDTO.class);
			
			List<PlanetDTO> planetsFromApi = response.getResults().stream()
					.filter(p -> p.getName().toUpperCase().trim().equals(name.toUpperCase().trim()))
					.collect(Collectors.toList());
			
			var filmsUrl = !planetsFromApi.isEmpty() ? planetsFromApi.get(0).getFilms() : new ArrayList<String>();
			return filmsUrl.stream().map(url -> {
				url = url.replace("http", "https");
				var film = restTemplate.getForObject(url, Film.class);
				return film;
			}).collect(Collectors.toSet());
			
		} catch (Exception ex) {
			//ex.printStackTrace();
			return new HashSet<>();
		}		
	}
}
