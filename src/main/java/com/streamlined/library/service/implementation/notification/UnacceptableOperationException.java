package com.streamlined.library.service.implementation.notification;

public class UnacceptableOperationException extends RuntimeException {

	public UnacceptableOperationException(String message) {
		super(message);
	}

	public UnacceptableOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
