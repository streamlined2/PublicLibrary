package com.streamlined.library.controller;

public class NoBookFoundException extends RuntimeException {

	public NoBookFoundException(String message) {
		super(message);
	}

	public NoBookFoundException(String message, Exception cause) {
		super(message, cause);
	}

}
