package com.rcksrs.swapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcksrs.swapi.exception.EntityNotFoundException;
import com.rcksrs.swapi.model.Planet;
import com.rcksrs.swapi.service.PlanetService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class PlanetControllerTest {
	
	@MockBean
	private PlanetService planetService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static ObjectMapper mapper;	
	private static Planet planet;
	private static List<Planet> planets;
	
	@BeforeAll
	static void beforeAll() {
		mapper = new ObjectMapper();
		planet = new Planet("1", "Tatooine", "Arid", "Dessert");
		planets = List.of(planet, new Planet("2", "Alderaan", "Temperate", "Grasslands"));
	}

	@Test
	@DisplayName("Should return 200 when find a planet by id")
	void testFindById() throws Exception {
		when(planetService.findById("1")).thenReturn(planet);
		
		mockMvc.perform(get("/planet/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(planet)));
	}
	
	@Test
	@DisplayName("Should return 404 when not find a planet by id")
	void testNotFindById() throws Exception {
		when(planetService.findById("1")).thenThrow(EntityNotFoundException.class);
		
		mockMvc.perform(get("/planet/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Should return 201 when save a planet")
	void testSave() throws Exception {
		when(planetService.save(any(Planet.class))).thenReturn(planet);
		
		mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(planet)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(planet)))
                .andExpect(header().string("Location", "http://localhost/planet/1"));
	}
	
	@Test
	@DisplayName("Should return 400 when validation fails")
	void testSaveInvalid() throws Exception {
		when(planetService.save(any(Planet.class))).thenReturn(planet);
		
		mockMvc.perform(post("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Planet())))
                .andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return 200 when find all planets")
	void testFindAll() throws Exception {
		when(planetService.findAll()).thenReturn(planets);
		
		mockMvc.perform(get("/planet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Planet())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
	}
	
	@Test
	@DisplayName("Should return 200 when delete a planet")
    void testeDelete() throws Exception {
		doNothing().when(planetService).delete("1");
		
        mockMvc.perform(delete("/planet")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(planet)))
                .andExpect(status().isOk());
    }

}
