package com.rcksrs.swapi.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rcksrs.swapi.exception.DuplicatedEntityException;
import com.rcksrs.swapi.exception.EntityNotFoundException;
import com.rcksrs.swapi.model.Planet;
import com.rcksrs.swapi.repository.PlanetRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PlanetServiceTest {
	
	@Mock
	private PlanetRepository planetRepository;
	
	@InjectMocks
	private PlanetService planetService;
	
	private static Planet planet;
	private static List<Planet> planets;
	
	@BeforeAll
	static void beforeAll() {
		planet = new Planet("1", "Tatooine", "Arid", "Dessert");
		planets = List.of(planet, new Planet("2", "Alderaan", "Temperate", "Grasslands"));
	}

	@Test
	@DisplayName("Should save a planet")
	void testSavePlanet() {
		when(planetRepository.findByNameIgnoreCase((any(String.class)))).thenReturn(Optional.empty());
		when(planetRepository.save(any(Planet.class))).thenReturn(planet);
		
		var newPlanet = new Planet("Tatooine", "Arid", "Dessert");
		var planetSaved = planetService.save(newPlanet);
		assertAll(() -> {
			assertEquals(newPlanet.getName(), planetSaved.getName());
			assertEquals(newPlanet.getClimate(), planetSaved.getClimate());
			assertEquals(newPlanet.getTerrain(), planetSaved.getTerrain());
			
		});
	}
	
	@Test
	@DisplayName("Should not save a existing planet")
	void testSaveDuplicatedPlanet() {
		when(planetRepository.findByNameIgnoreCase((any(String.class)))).thenReturn(Optional.of(planet));
		
		var newPlanet = new Planet("Tatooine", "Arid", "Dessert");
		assertThrows(DuplicatedEntityException.class, () -> planetService.save(newPlanet));	
	}
	
	@Test
	@DisplayName("Should update a planet")
	void testUpdatePlanet() {
		when(planetRepository.save(any(Planet.class))).thenReturn(planet);
		when(planetRepository.findById(any(String.class))).thenReturn(Optional.of(planet));
		
		var newPlanet = new Planet("1", "Tatooine", "Arid", "Dessert");
		var planetSaved = planetService.update(newPlanet);
		assertAll(() -> {
			assertEquals(newPlanet.getName(), planetSaved.getName());
			assertEquals(newPlanet.getClimate(), planetSaved.getClimate());
			assertEquals(newPlanet.getTerrain(), planetSaved.getTerrain());
			
		});		
	}
	
	@Test
	@DisplayName("Should not update a planet when id is missing")
	void testNotUpdatePlanet() {
		when(planetRepository.save(any(Planet.class))).thenReturn(planet);
		
		var newPlanet = new Planet("Tatooine", "Arid", "Dessert");
		assertThrows(EntityNotFoundException.class, () -> planetService.update(newPlanet))	;
	}
	
	@Test
	@DisplayName("Should not update a no existing planet")
	void testNotUpdatePlanetNotFound() {
		when(planetRepository.findById(any(String.class))).thenReturn(Optional.empty());
		
		var newPlanet = new Planet("1", "Tatooine", "Arid", "Dessert");
		assertThrows(EntityNotFoundException.class, () -> planetService.update(newPlanet));
	}	
	
	@Test
	@DisplayName("Should return a list of planets")
	void testFindAllPlanets() {
		when(planetRepository.findAll(any(Sort.class))).thenReturn(planets);
		
		var planetsSaved = planetService.findAll();
		assertTrue(planetsSaved.size() == 2);	
	}
	
	@Test
	@DisplayName("Should delete a planet")
	void testDeletePlanet() {
		when(planetRepository.findById(any(String.class))).thenReturn(Optional.of(planet));
		assertDoesNotThrow(() -> planetService.delete("1"));	
	}
	
	@Test
	@DisplayName("Should not delete a planet")
	void testNotDeletePlanet() {
		when(planetRepository.findById(any(String.class))).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> planetService.delete("1"));	
	}

}
