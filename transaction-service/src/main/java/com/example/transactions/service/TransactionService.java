package com.example.transactions.service;

import com.example.transactions.enums.TransactionStatus;
import com.example.transactions.model.Transaction;
import com.example.transactions.model.User;
import com.example.transactions.repo.TransactionRepository;
import com.example.utility.NotificationKafkaDto;
import com.example.utility.TxnKafkaDTO;
import com.example.utility.WalletKafkaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import static com.example.utility.CommonConstants.*;
import static com.example.utility.WalletUpdateStatus.SUCCESS;

@Service
@Slf4j
public class TransactionService implements UserDetailsService{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	KafkaTemplate<String,Object> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	RestTemplate restTemplate;

	
	public String initiateTransaction(String sender,String receiver, String purpose, Double amount) throws JsonProcessingException {
		
		Transaction transaction = Transaction.builder()
				.sender(sender)
				.receiver(receiver)
				.purpose(purpose)
				.transactionId(Uuid.randomUuid().toString())
				.transactionStatus(TransactionStatus.PENDING)
				.amount(amount)
				.build();
		
		transactionRepository.save(transaction);
		log.info("saved data in database where txn {}",transaction);
		//publish the event post after initiating Transaction which will be listened by consumers
		TxnKafkaDTO txnKafkaDTO = new TxnKafkaDTO(sender,receiver,amount,transaction.getTransactionId());
		kafkaTemplate.send(TRANSACTION_CREATION_TOPIC,
				txnKafkaDTO);

		log.info("publish events in Kafka {}",txnKafkaDTO);
		return transaction.getTransactionId();
		
	}
	
	@KafkaListener(topics=WALLET_UPDATED_TOPIC, groupId="EWallet_Group")
	public void updateTransaction(String msg) {
		try {
			WalletKafkaDto walletKafkaDto = objectMapper.readValue(msg, WalletKafkaDto.class);
			User user = getUserFromUserService(walletKafkaDto.getSender());
			String senderEmail = user.getEmail();
			String receiverEmail = null;
			if (SUCCESS.equals(walletKafkaDto.getWalletUpdateStatus())) {
				User usr = getUserFromUserService(walletKafkaDto.getReceiver());
				receiverEmail = usr.getEmail() ;
				transactionRepository.updateTransaction(walletKafkaDto.getTxnId(), TransactionStatus.SUCCESS);
			} else {
				transactionRepository.updateTransaction(walletKafkaDto.getTxnId(), TransactionStatus.FAILED);
			}
			String senderMsg = "Hi, your transaction with id " + walletKafkaDto.getTxnId() + " got " + walletKafkaDto.getWalletUpdateStatus();
			NotificationKafkaDto senderKafkaDto = new NotificationKafkaDto(senderEmail,senderMsg);
			kafkaTemplate.send(TRANSACTION_COMPLETION_TOPIC, senderKafkaDto);

			if (SUCCESS.equals(walletKafkaDto.getWalletUpdateStatus())) {
				String receiverMsg = "Hi, you have received Rs." + walletKafkaDto.getAmount() + " from " +
						walletKafkaDto.getSender() + " in your wallet linked with phone number " + walletKafkaDto.getReceiver();

				NotificationKafkaDto receiverKafkaDto = new NotificationKafkaDto(receiverEmail,receiverMsg);
				kafkaTemplate.send(TRANSACTION_COMPLETION_TOPIC,
						receiverKafkaDto);
			}
		}
		catch (Exception e) {
			log.error("exception occurred ",e);
			throw  new RuntimeException("getting exception ",e.getCause());
		}
	   
	}
	
	
	//getting userdetails from user Service to Transaction Service 
	private User getUserFromUserService(String username) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth("admin","admin123");
		HttpEntity request = new HttpEntity(httpHeaders);
		String url = "http://localhost:8086/admin/user/"+username;
		return restTemplate.exchange(url, HttpMethod.GET,
				request, User.class).getBody();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new CustomUserDetails(getUserDetails(username));
	}

	public User getUserDetails(String username) {
		User user = getUserFromUserService(username);
		return user;
	}
}
