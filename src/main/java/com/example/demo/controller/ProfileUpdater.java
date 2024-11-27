package com.example.demo.controller;

import com.example.demo.models.AuthenticationServerResponse;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileUpdater {

    private final UserRepository userRepository;

    @Autowired
    public ProfileUpdater (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @KafkaListener(topics = "auth_response", containerFactory = "kafkaListenerApiGateWayService")
    public void updateToken(ConsumerRecord<String, String> message) {
        System.err.println("updateToken key: " + message.key() + " value: "+ message.value());
        AuthenticationServerResponse response = new AuthenticationServerResponse(message.value());

        Optional<User> users = userRepository.findById(response.getUser_id());

        if(users.isEmpty()) {
            System.err.println("user not found: " + response.getUser_id());
            return;
        }
        User user = users.get();
        user.setToken(response.getToken());
        System.err.println("update user: " + user.getId() + " " + user.getFirstname());
        userRepository.save(user);
    }
}
