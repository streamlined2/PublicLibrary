package com.streamlined.library.service.implementation.notification;

import java.util.List;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.streamlined.library.service.implementation.notification.event.Event;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaSender implements Sender {

	private final KafkaAdmin kafkaAdmin;
	private final KafkaTemplate<String, Event> kafkaTemplate;
	private final List<NewTopic> topicList;

	private KafkaSender(@Value("${spring.kafka.number-of-partitions}") int numberOfPartitions,
			@Value("${spring.kafka.replication-factor}") short replicationFactor, KafkaAdmin kafkaAdmin,
			KafkaTemplate<String, Event> kafkaTemplate) {
		this.kafkaAdmin = kafkaAdmin;
		this.kafkaTemplate = kafkaTemplate;
		this.topicList = List.of(new NewTopic("new-customer-created", numberOfPartitions, replicationFactor),
				new NewTopic("request-received", numberOfPartitions, replicationFactor),
				new NewTopic("approval-received", numberOfPartitions, replicationFactor),
				new NewTopic("transfer-accomplished", numberOfPartitions, replicationFactor),
				new NewTopic("return-accomplished", numberOfPartitions, replicationFactor));
	}

	@Override
	public void send(Event event) {
		final var topic = mapEventToTopic(event);
		kafkaAdmin.createOrModifyTopics(topic);
		var result = kafkaTemplate.send(topic.name(), event);
		result.whenComplete((rst, exc) -> {
			if (exc != null) {
				log.error("impossible to send message {} to Kafka topic {}", rst.getProducerRecord().value(),
						rst.getProducerRecord().topic());
				throw new CantSendKafkaMessageException("impossible to send message %s to Kafka topic %s"
						.formatted(rst.getProducerRecord().value().toString(), rst.getProducerRecord().topic()), exc);
			}
		});
	}

	@Override
	public boolean accepts(Class<? extends Event> eventClass) {
		return true;
	}

	private NewTopic mapEventToTopic(Event event) {
		try {
			return switch (event.getClass().getSimpleName()) {
			case "NewCustomerCreatedEvent" -> topicList.get(0);
			case "RequestReceivedEvent" -> topicList.get(1);
			case "ApprovalReceivedEvent" -> topicList.get(2);
			case "TransferAccomplishedEvent" -> topicList.get(3);
			case "ReturnAccomplishedEvent" -> topicList.get(4);
			default -> {
				log.error("no Kafka topic found for event type {}", event.getClass().getSimpleName());
				throw new NoKafkaTopicFoundException(
						"no Kafka topic found for event type %s".formatted(event.getClass().getSimpleName()));
			}
			};
		} catch (IndexOutOfBoundsException e) {
			log.error("there is no assigned topic for event {}", event.getClass().getSimpleName());
			throw new NoKafkaTopicFoundException(
					"there is no assigned topic for event %s".formatted(event.getClass().getSimpleName()));
		}

	}

}
