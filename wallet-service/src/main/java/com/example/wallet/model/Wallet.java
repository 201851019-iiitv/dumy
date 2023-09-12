package com.example.wallet.model;

import com.example.utility.UserIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private Long userId;
	
	private String phoneNumber;
	
	private Double balance;
	
	@Enumerated(value=EnumType.STRING)
	private UserIdentifier userIdentifier;

	private String identifierValue;
	
}


