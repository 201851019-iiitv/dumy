package com.example.notification.service;

import com.example.utility.NotificationKafkaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.utility.CommonConstants.TRANSACTION_COMPLETION_TOPIC;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	SimpleMailMessage simpleMailMessage;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@KafkaListener(topics = {TRANSACTION_COMPLETION_TOPIC}, groupId = "grp123")
	public void sendNotification(String msg) {
		try {
			NotificationKafkaDto notificationKafkaDto = objectMapper.readValue(msg, NotificationKafkaDto.class);
			simpleMailMessage.setFrom("csetestsoftware18@gmail.com");
			simpleMailMessage.setTo(notificationKafkaDto.getEmail());
			simpleMailMessage.setText(notificationKafkaDto.getMsg());
			simpleMailMessage.setSubject("E-Wallet Payment Updates");
			javaMailSender.send(simpleMailMessage);
			log.info("Notification send successfully");
		}
		catch (Exception e) {
			log.error("exception occurred ",e);
			throw  new RuntimeException("getting exception ",e.getCause());
		}

	}
	
	
}
