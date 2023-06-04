package com.streamlined.library.service.implementation.notification;

import java.util.List;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSender implements Sender {

	private final @Qualifier("topicList") List<NewTopic> topicList;
	private final KafkaAdmin kafkaAdmin;
	private final KafkaTemplate<String, Message> kafkaTemplate;

	@Override
	public void send(Message message) {
		final var topic = getTopic(message);
		kafkaAdmin.createOrModifyTopics(topic);
		var result = kafkaTemplate.send(topic.name(), message);
		result.whenComplete((rst, exc) -> {
			if (exc != null) {
				log.error("impossible to send message {} to Kafka topic {}", rst.getProducerRecord().value(),
						rst.getProducerRecord().topic());
				throw new CantSendKafkaMessageException("impossible to send message %s to Kafka topic %s"
						.formatted(rst.getProducerRecord().value(), rst.getProducerRecord().topic()), exc);
			}
		});
	}

	private NewTopic getTopic(Message message) {
		final var order = message.messageType().ordinal();
		if (order >= topicList.size()) {
			log.error("no Kafka topic found for message type {}, index {} greater than number of allocated topics {}",
					message.messageType().name(), order, topicList.size());
			throw new NoKafkaTopicFoundException(
					"no Kafka topic found for message type %s, index %d greater than number of allocated topics %d"
							.formatted(message.messageType().name(), order, topicList.size()));
		}
		return topicList.get(order);
	}

}
