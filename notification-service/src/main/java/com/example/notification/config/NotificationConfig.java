package com.example.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class NotificationConfig {

	Properties getProperties() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return properties;
	}
	
	@Bean
	ConsumerFactory getConsumerFactory() {
		return new DefaultKafkaConsumerFactory(getProperties());
	}
	
	@Bean
	SimpleMailMessage getMailMessage() {
		return new SimpleMailMessage();
	}
	
	@Bean
	JavaMailSender getMailSender() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		javaMailSender.setUsername("csetestsoftware18@gmail.com");
		javaMailSender.setPassword("9506998616");
		
		Properties properties = javaMailSender.getJavaMailProperties();
		properties.put("mail.smtp.starttls.enable", true); // this step is Mandatory
		properties.put("mail.debug", true); // this is for debugging purpose
		
		return javaMailSender;
	}
	
}













