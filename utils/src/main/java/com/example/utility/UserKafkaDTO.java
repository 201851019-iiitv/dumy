package com.example.utility;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserKafkaDTO implements Serializable {

    private Integer userId;
    private String phNumber;
    private String identifierValue;
    private UserIdentifier userIdentifier;
}
