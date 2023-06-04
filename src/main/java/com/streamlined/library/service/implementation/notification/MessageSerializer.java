package com.streamlined.library.service.implementation.notification;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.kafka.common.serialization.Serializer;

import com.streamlined.library.service.implementation.notification.event.CustomerRelated;
import com.streamlined.library.service.implementation.notification.event.Event;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSerializer implements Serializer<Event> {

	private static final int INITIAL_BYTE_STREAM_SIZE = 1000;

	@Override
	public byte[] serialize(String topic, Event event) {
		try (var byteStream = new ByteArrayOutputStream(INITIAL_BYTE_STREAM_SIZE);
				var dataStream = new DataOutputStream(byteStream)) {
			if (event instanceof CustomerRelated customerRelatedEvent) {
				dataStream.writeLong(customerRelatedEvent.getCustomer().getId());
				dataStream.writeChars(customerRelatedEvent.getCustomer().getLogin());
			}
			dataStream.writeChars(event.getClass().getSimpleName());
			for (var bookId : event.getBookIds()) {
				dataStream.writeLong(bookId);
			}
			return byteStream.toByteArray();
		} catch (IOException e) {
			log.error("impossible to serialize Kafka message {}", event.toString());
			throw new KafkaMessageSerializationException(
					"impossible to serialize Kafka message %s".formatted(event.toString()), e);
		}
	}

}
