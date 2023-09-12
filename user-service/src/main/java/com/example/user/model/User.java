package com.example.user.model;

import com.example.utility.UserIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    @NotBlank(message = "User name can't be blank")
    private String username;
    @Column(unique = true)
    @Pattern(regexp = "\\d+",message = "phNumber can be digit with length only 10")
    @Size(max = 10, min = 10)
    private String phoneNumber;

    @Column(unique = true)
    @NotBlank(message = "email can't be blank")
    private String email;

    @NotBlank(message = "password can't be blank")
    private String password;

    private String dob;

    private String country;

    //TODO handle this
    //@Pattern(regexp = "[adm,srv,usr]")
    private String authorities;

    @Enumerated(value = EnumType.STRING)
    private UserIdentifier userIdentifier;

    @Column(unique = true)
    private String identifierValue;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;



}

