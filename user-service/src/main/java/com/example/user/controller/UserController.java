package com.example.user.controller;

import com.example.user.Service.CustomUserDetails;
import com.example.user.Service.UserService;
import com.example.user.model.User;
import com.example.user.model.UserCreateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) throws JsonProcessingException {
        userService.create(userCreateRequest);
        System.out.println("User created successfully");
        return new ResponseEntity<>("Successfully created the user",HttpStatus.OK);
    }


    @GetMapping("/user")
    public User getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails  user = (CustomUserDetails) authentication.getPrincipal();
        return userService.getUserByUsername(user.getUsername());

    }


    @GetMapping("/admin/all/users")
    public List<User> getAllUsersDetails() {
        return userService.getAll();
    }


    @GetMapping("/admin/user/{userName}")
    public User getUserDetails(@PathVariable("userName") String userName) {
        if(userName.length()== 10)
            return userService.getUserByPhNumber(userName);
        return userService.getUserByUsername(userName);
    }


}
