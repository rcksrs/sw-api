package com.rcksrs.swapi.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionMessage {
	private String message;
	private HttpStatus statusCode;
	private List<String> errors;
}
