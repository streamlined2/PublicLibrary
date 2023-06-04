package com.streamlined.library;

import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.streamlined.library.service.implementation.notification.MessageSerializer;
import com.streamlined.library.service.implementation.notification.event.Event;

@Configuration
public class KafkaConfiguration {

	private @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers));
	}

	@Bean
	public ProducerFactory<String, Event> producerFactory() {
		return new DefaultKafkaProducerFactory<>(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class));
	}

	@Bean
	public KafkaTemplate<String, Event> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
