package com.example.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxnKafkaDTO implements Serializable {
    private String sender;
    private String receiver;
    private Double amount;
    private String TxnId;
}
