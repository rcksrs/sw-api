package com.rcksrs.swapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlanetDTO {
	private String name;
	private List<String> films;

}
