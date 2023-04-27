package com.streamlined.library;

public class WrongRequestParameterException extends RuntimeException {

	public WrongRequestParameterException(String message) {
		super(message);
	}

	public WrongRequestParameterException(String message, Exception cause) {
		super(message, cause);
	}

}
