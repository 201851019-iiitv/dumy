package com.example.transactions.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class TransactionConfig {

	@Bean
	ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	RestTemplate getTemplate() {
		return new RestTemplate();
	}
	
	Properties getConsumerProperties(){
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return properties;
	}
	
	@Bean
	ConsumerFactory getConsumerFactory() {
		return new DefaultKafkaConsumerFactory(getConsumerProperties());
	}
	
	
	Properties getProducerProperties() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return properties;
	}
	
	
	ProducerFactory getProducerFactory() {
		return new DefaultKafkaProducerFactory(getProducerProperties());
	}
	
	@Bean
	KafkaTemplate<String,Object> kafkaTemplate(){
		return new KafkaTemplate(getProducerFactory());
	}
	
}








