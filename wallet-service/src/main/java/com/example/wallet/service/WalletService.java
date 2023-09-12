package com.example.wallet.service;

import com.example.utility.TxnKafkaDTO;
import com.example.utility.UserKafkaDTO;
import com.example.utility.WalletKafkaDto;
import com.example.utility.WalletUpdateStatus;
import com.example.wallet.model.Wallet;
import com.example.wallet.repo.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.utility.CommonConstants.*;
import static com.example.utility.WalletUpdateStatus.FAILED;
import static com.example.utility.WalletUpdateStatus.SUCCESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
     KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @KafkaListener(topics = USER_CREATION_TOPIC, groupId = "EWallet_Group")
    public void createWallet(String msg) {
        try {
            UserKafkaDTO userKafkaDTO = objectMapper.readValue(msg, UserKafkaDTO.class);
            Wallet wallet = Wallet.builder()
                    .userId(Long.valueOf(userKafkaDTO.getUserId()))
                    .phoneNumber(userKafkaDTO.getPhNumber())
                    .userIdentifier(userKafkaDTO.getUserIdentifier())
                    .identifierValue(userKafkaDTO.getIdentifierValue())
                    .balance(10.0)
                    .build();
            walletRepository.save(wallet);
        } catch (Exception e) {
            log.error("exception : ", e);
            throw new RuntimeException("getting exception while parsing", e.getCause());
        }
    }


    @KafkaListener(topics = TRANSACTION_CREATION_TOPIC, groupId = "EWallet_Group")
    public void updateWalletForTransaction(String msg) {
        try {
            TxnKafkaDTO txnKafkaDTO = objectMapper.readValue(msg, TxnKafkaDTO.class);
            log.info("Validating Sender's Wallet Balance : sender - {}, receiver - {}, amount - {},transactionId - {}",
                    txnKafkaDTO.getSender(), txnKafkaDTO.getReceiver(), txnKafkaDTO.getAmount(), txnKafkaDTO.getTxnId());

            Wallet senderWallet = walletRepository.findByPhoneNumber(txnKafkaDTO.getSender());
            Wallet receiverWallet = walletRepository.findByPhoneNumber(txnKafkaDTO.getReceiver());

            //publish the event after validating and updating wallets of sender and receiver
            WalletUpdateStatus walletUpdateStatus;
            if (senderWallet == null || receiverWallet == null || senderWallet.getBalance() <= txnKafkaDTO.getAmount()) {
                walletUpdateStatus = FAILED;
            }

            //Todo why amount is zero ?
            walletRepository.updateWallet(txnKafkaDTO.getSender(), 0 - txnKafkaDTO.getAmount());
            walletRepository.updateWallet(txnKafkaDTO.getReceiver(), txnKafkaDTO.getAmount());
            walletUpdateStatus = SUCCESS;
            WalletKafkaDto walletKafkaDto = new WalletKafkaDto(txnKafkaDTO.getSender(), txnKafkaDTO.getReceiver(), txnKafkaDTO.getAmount(), txnKafkaDTO.getTxnId(), walletUpdateStatus);

            kafkaTemplate.send(WALLET_UPDATED_TOPIC,
                    walletKafkaDto);

        } catch (Exception e) {
            log.error("exception occurred ", e);
            throw new RuntimeException("getting exception ", e.getCause());
        }
    }


}
