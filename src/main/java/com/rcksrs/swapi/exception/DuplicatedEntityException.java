package com.rcksrs.swapi.exception;

public class DuplicatedEntityException extends RuntimeException {
	private static final long serialVersionUID = 7773601647329111215L;
	
	public DuplicatedEntityException() {
		super("Planet already exists");
	}
	
	public DuplicatedEntityException(String message) {
		super(message);
	}

}
