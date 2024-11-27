package com.example.demo.controller;

import com.example.demo.models.AuthenticationServerResponse;
import com.example.demo.models.User;
import com.example.demo.models.UserRequest;
import com.example.demo.models.UserResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.kafka.KafkaProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class ProfileController {

    private final KafkaProducerService kafkaProducerService;
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(KafkaProducerService kafkaProducerService, UserRepository userRepository) {
        this.kafkaProducerService = kafkaProducerService;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "user_info", containerFactory = "kafkaListenerAuthService")
    public void getInfo(ConsumerRecord<String, String> message) {
        try {
            UserRequest userRequest = new UserRequest(message.value());
            UserResponse response = new UserResponse();

            Optional<User> user = userRepository.findByToken(userRequest.getAuthorizationHeader());

            if(user.isEmpty()){
                System.err.println("getInfo User not found: " + userRequest.getAuthorizationHeader());
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
            } else {
                System.err.println("getInfo: User found");
                response.setFirstname(user.get().getFirstname());
                response.setLastname(user.get().getLastname());
            }

            kafkaProducerService.sendMessage("user_response",message.key(),response.toString());
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


}