package com.streamlined.library;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

	private @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers;
	private @Value("${spring.kafka.number-of-partitions}") int numberOfPartitions;
	private @Value("${spring.kafka.replication-factor}") short replicationFactor;

	@Bean
	public List<NewTopic> topicList() {
		return List.of(new NewTopic("new-customer-created", numberOfPartitions, replicationFactor),
				new NewTopic("request-received", numberOfPartitions, replicationFactor),
				new NewTopic("approval-received", numberOfPartitions, replicationFactor),
				new NewTopic("transfer-accomplished", numberOfPartitions, replicationFactor),
				new NewTopic("return-accomplished", numberOfPartitions, replicationFactor));
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers));
	}

	@Bean
	public NewTopics newTopics() {
		List<NewTopic> topicList = topicList();
		return new NewTopics(topicList.toArray(new NewTopic[topicList.size()]));
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
