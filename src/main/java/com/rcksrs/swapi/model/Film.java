package com.rcksrs.swapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"title"})
public class Film {
	private String title;
	private String url;
	private String director;
	private String producer;
	
	@JsonAlias({"episode_id"})
	private Integer episodeId;	
	
	@JsonAlias({"opening_crawl"})
	private String openingCrawl;

}
