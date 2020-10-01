package com.rcksrs.swapi.exception;

public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7773601647329111215L;
	
	public EntityNotFoundException() {
		super("Planet not found");
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}

}
