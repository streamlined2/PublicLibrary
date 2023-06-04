package com.streamlined.library.service.implementation.notification;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.kafka.common.serialization.Serializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSerializer implements Serializer<Message> {

	private static final int INITIAL_BYTE_STREAM_SIZE = 1000;

	@Override
	public byte[] serialize(String topic, Message message) {
		try (var byteStream = new ByteArrayOutputStream(INITIAL_BYTE_STREAM_SIZE);
				var dataStream = new DataOutputStream(byteStream)) {
			dataStream.writeLong(message.customer().getId());
			dataStream.writeChars(message.customer().getLogin());
			dataStream.writeChars(message.messageType().name());
			for (var bookId : message.getBookIds()) {
				dataStream.writeLong(bookId);
			}
			return byteStream.toByteArray();
		} catch (IOException e) {
			log.error("impossible to serialize Kafka message {}", message.toString());
			throw new KafkaMessageSerializationException(
					"impossible to serialize Kafka message %s".formatted(message.toString()), e);
		}
	}

}
