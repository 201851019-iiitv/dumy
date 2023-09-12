package com.example.transactions.model;

import com.example.transactions.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String transactionId;
	
	//sender and receivers are usually user id's of
	// our application
	private String sender;
	
	private String receiver;
	
	private String purpose;
	
	private Double amount;
	
	@Enumerated(value=EnumType.STRING)
	private TransactionStatus transactionStatus;
	
	@CreationTimestamp
	private Date createdOn;
	
	@UpdateTimestamp
	private Date updatedOn;
	
	
}
