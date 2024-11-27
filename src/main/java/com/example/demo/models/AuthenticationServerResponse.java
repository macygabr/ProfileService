package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationServerResponse {
    @JsonProperty("user_id")
    private Long user_id = 0L;

    @JsonProperty("token")
    private String token = UUID.randomUUID().toString();

    public AuthenticationServerResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            AuthenticationServerResponse response = new ObjectMapper().readValue(message, AuthenticationServerResponse.class);
            this.user_id = response.getUser_id();
            this.token = response.getToken();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }
}
