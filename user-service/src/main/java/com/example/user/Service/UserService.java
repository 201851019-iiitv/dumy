package com.example.user.Service;

import com.example.user.model.User;
import com.example.user.model.UserCreateRequest;
import com.example.user.repo.UserRepository;
import com.example.utility.UserKafkaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.example.user.constants.UserConstants.USER_AUTHORITY;
import static com.example.utility.CommonConstants.USER_CREATION_TOPIC;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
     UserRepository userRepository;

    @Autowired
     KafkaTemplate<String, Object> kafkaTemplate;;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findUserByUsername(username);
      if(ObjectUtils.isEmpty(user))
          throw  new UsernameNotFoundException("User Not Found");
      return new CustomUserDetails(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if(ObjectUtils.isEmpty(user))
            throw  new UsernameNotFoundException("User Not Found");
        return user;
    }

    public User getUserByPhNumber(String phNumber) {
        User user = userRepository.findByPhoneNumber(phNumber);
        if(ObjectUtils.isEmpty(user))
            throw  new UsernameNotFoundException("User Not Found");
        return user;
    }

    public void create(UserCreateRequest userCreateRequest) throws JsonProcessingException {
        User user = userCreateRequest.toUser();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setAuthorities(USER_AUTHORITY);
        //object is saved in the database
        user = userRepository.save(user);

        //publish the event post user creation which will be listened by consumers
        UserKafkaDTO userKafkaDTO = prepareKafkaDto(user);
        kafkaTemplate.send(USER_CREATION_TOPIC,
                userKafkaDTO);

    }

    private UserKafkaDTO prepareKafkaDto(User user) {
        UserKafkaDTO userKafkaDTO = new UserKafkaDTO();
        userKafkaDTO.setUserId(user.getId());
        userKafkaDTO.setUserIdentifier(user.getUserIdentifier());
        userKafkaDTO.setPhNumber(user.getPhoneNumber());
        userKafkaDTO.setIdentifierValue(user.getIdentifierValue());
        return userKafkaDTO;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }


}
