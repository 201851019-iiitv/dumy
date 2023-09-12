package com.example.user.repo;

import com.example.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByPhoneNumber(String phoneNumber);

    User findUserByUsername(String username);

}
