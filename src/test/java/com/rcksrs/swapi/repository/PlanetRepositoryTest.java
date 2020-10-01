package com.rcksrs.swapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rcksrs.swapi.model.Planet;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PlanetRepositoryTest {
	
	@Autowired
	private PlanetRepository planetRepository;
	
	private Planet planet;
	
	@BeforeEach
	void beforeRun() {
		planet = planetRepository.save(new Planet("Tatooine", "Arid", "Dessert"));
	}
	
	@AfterEach
	public void afterRun() {
		planetRepository.deleteById(planet.getId());
	}
	
	@Test
	@DisplayName("Should find a planet by Name")
	void testFindByName() {
		var planetSaved = planetRepository.findByNameIgnoreCase("Tatooine");
		
		assertTrue(planetSaved.isPresent());
		assertEquals("Tatooine", planetSaved.get().getName());
	}
	
	@Test
	@DisplayName("Should find two planets when filter by name")
	void testFilterByName() {
		var newPlanet = planetRepository.save(new Planet("Tatooine 2", "Arid", "Dessert"));
		var planetSaved = planetRepository.findByNameContainingIgnoreCase("Tat");
		
		assertTrue(planetSaved.size() == 2);
		planetRepository.deleteById(newPlanet.getId());
	}

}
