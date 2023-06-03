package com.streamlined.library.service.implementation.notification;

public class NoKafkaTopicFoundException extends RuntimeException {

	public NoKafkaTopicFoundException(String message) {
		super(message);
	}

	public NoKafkaTopicFoundException(String message, Exception cause) {
		super(message, cause);
	}

}
