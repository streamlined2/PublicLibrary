package com.streamlined.library.controller;

import org.springframework.dao.DataAccessException;

public class NoEntityFoundException extends DataAccessException {

	public NoEntityFoundException(String message) {
		super(message);
	}

	public NoEntityFoundException(String message, Exception cause) {
		super(message, cause);
	}

}
