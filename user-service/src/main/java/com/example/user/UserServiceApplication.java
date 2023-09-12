package com.example.user;

import com.example.user.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    // for converting JSONObject to string vice versa
    @Bean
    ObjectMapper getMapper() {
        return new ObjectMapper();
    }
    @Override
   public void run(String... args) throws Exception {
//	   User testUser = User.builder()
//			   .phoneNumber("1234567809")
//               .username("admin")
//			   .password(this.bCryptPasswordEncoder.encode("admin123"))
//			   .authorities(ADMIN_AUTHORITY)
//			   .email("admin@gmail.com")
//			   .userIdentifier(SERVICE_ID)
//			   .identifierValue("admin45")
//			   .build();
//        User userRole = User.builder()
//                .phoneNumber("1234567908")
//                .username("user")
//                .password(this.bCryptPasswordEncoder.encode("user123"))
//                .authorities(USER_AUTHORITY)
//                .email("user@gmail.com")
//                .userIdentifier(SERVICE_ID)
//                .identifierValue("user45")
//                .build();
//        User  serviceRole= User.builder()
//                .phoneNumber("1234568907")
//                .username("service")
//                .password(this.bCryptPasswordEncoder.encode("service123"))
//                .authorities(SERVICE_AUTHORITY)
//                .email("service@gmail.com")
//                .userIdentifier(SERVICE_ID)
//                .identifierValue("service45")
//                .build();
//
//	    userRepository.save(testUser);
//        userRepository.save(userRole);
//        userRepository.save(serviceRole);
    }

}