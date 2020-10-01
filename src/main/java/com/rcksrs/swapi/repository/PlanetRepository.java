package com.rcksrs.swapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rcksrs.swapi.model.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String> {
	Optional<Planet> findByNameIgnoreCase(String name);
	List<Planet> findByNameContainingIgnoreCase(String name);

}
