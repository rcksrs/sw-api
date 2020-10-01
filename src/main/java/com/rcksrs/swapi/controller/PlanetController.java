package com.rcksrs.swapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rcksrs.swapi.model.Planet;
import com.rcksrs.swapi.service.PlanetService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/planet")
public class PlanetController {
	
	@Autowired
	private PlanetService planetService;
	
	@GetMapping
	@ApiOperation(value = "Return a list of planets saved")
	public ResponseEntity<List<Planet>> index() {
		var planets = planetService.findAll();
		return ResponseEntity.ok(planets);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Find a planet by planetId")
	public ResponseEntity<Planet> findById(@PathVariable String id) {
		var planet = planetService.findById(id);
		return ResponseEntity.ok(planet);
	}
	
	@GetMapping("/name/{name}")
	@ApiOperation(value = "Find a planet by name")
	public ResponseEntity<Planet> findByName(@PathVariable String name) {
		var planet = planetService.findByName(name);
		return ResponseEntity.ok(planet);
	}
	
	@GetMapping("/name")
	@ApiOperation(value = "Filter a planet by name")
	public ResponseEntity<List<Planet>> findAllByName(@RequestParam String name) {
		var planets = planetService.filterByName(name);
		return ResponseEntity.ok(planets);
	}
	
	@PostMapping
	@ApiOperation(value = "Save a planet")
	public ResponseEntity<Planet> save(@RequestBody @Valid Planet planet) {
		var planetSaved = planetService.save(planet);
		var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(planetSaved.getId()).toUri();
		return ResponseEntity.created(location).body(planetSaved);
	}
	
	@PutMapping
	@ApiOperation(value = "Update a planet")
	public ResponseEntity<Planet> update(@RequestBody @Valid Planet planet) {
		var planetSaved = planetService.update(planet);
		var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(planetSaved.getId()).toUri();
		return ResponseEntity.created(location).body(planetSaved);
	}
	
	@DeleteMapping
	@ApiOperation(value = "Delete a planet")
	public ResponseEntity<Object> delete(@RequestBody @Valid Planet planet) {
		planetService.delete(planet.getId());
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
