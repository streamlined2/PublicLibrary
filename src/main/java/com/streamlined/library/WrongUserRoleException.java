package com.streamlined.library;

public class WrongUserRoleException extends RuntimeException {

	public WrongUserRoleException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongUserRoleException(String message) {
		super(message);
	}

}
