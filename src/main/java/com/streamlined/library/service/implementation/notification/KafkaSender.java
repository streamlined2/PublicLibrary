package com.streamlined.library.service.implementation.notification;

import java.util.List;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaSender implements Sender {

	private final @Qualifier("topicList") List<NewTopic> topicList;
	private final KafkaAdmin kafkaAdmin;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void send(Message message) {
		final var topic = getTopic(message);
		kafkaAdmin.createOrModifyTopics(topic);
		kafkaTemplate.send(topic.name(), getData(message));
	}

	private NewTopic getTopic(Message message) {
		final var order = message.messageType().ordinal();
		if (order >= topicList.size()) {
			throw new NoKafkaTopicFoundException(
					"no Kafka topic found for message type %s, index %d greater than number of allocated topics %d"
							.formatted(message.messageType().name(), order, topicList.size()));
		}
		return topicList.get(order);
	}

	private String getData(Message message) {
		return "%s: %s".formatted(message.getTopic(), message.getFormattedText());
	}

}
