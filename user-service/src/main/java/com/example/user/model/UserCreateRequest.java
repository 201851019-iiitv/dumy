package com.example.user.model;

import com.example.utility.UserIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String dob;

    private String country;

    @NotNull
    private UserIdentifier userIdentifier;

    @NotBlank
    private String identifierValue;

    private String username;

    public User toUser() {
        return User.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .password(this.password)
                .email(this.email)
                .country(this.country)
                .dob(this.dob)
                .userIdentifier(this.userIdentifier)
                .identifierValue(this.identifierValue)
                .username(this.username)
                .build();
    }

}






