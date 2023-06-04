package com.streamlined.library.service.implementation.notification;

public class CantSendKafkaMessageException extends RuntimeException {

	public CantSendKafkaMessageException(String message) {
		super(message);
	}

	public CantSendKafkaMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
