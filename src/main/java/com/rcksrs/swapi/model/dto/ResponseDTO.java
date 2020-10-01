package com.rcksrs.swapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResponseDTO {
	private List<PlanetDTO> results;
	
}
