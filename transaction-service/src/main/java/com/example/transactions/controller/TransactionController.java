package com.example.transactions.controller;

import com.example.transactions.model.User;
import com.example.transactions.service.CustomUserDetails;
import com.example.transactions.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
		
	
	@Autowired
	TransactionService transactionService;
	
	
	@PostMapping("/transact")
	public ResponseEntity<String> initiateTransaction(@RequestParam("receiver") String receiver,
											  @RequestParam("purpose") String purpose,
											  @RequestParam("amount") Double amount) throws JsonProcessingException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		User user1 =  transactionService.getUserDetails(user.getUsername());
		return new ResponseEntity<>(transactionService.initiateTransaction(user1.getPhoneNumber(),receiver,purpose,amount), HttpStatus.OK);
	}

	
	
	
}
