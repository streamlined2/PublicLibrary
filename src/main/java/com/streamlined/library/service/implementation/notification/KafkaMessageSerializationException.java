package com.streamlined.library.service.implementation.notification;

public class KafkaMessageSerializationException extends RuntimeException {

	public KafkaMessageSerializationException(String message) {
		super(message);
	}

	public KafkaMessageSerializationException(String message, Exception cause) {
		super(message, cause);
	}

}
