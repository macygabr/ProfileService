package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    @JsonProperty("authorizationHeader")
    private String authorizationHeader;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public UserRequest(String massage) {
        try {
            UserRequest signInRequest= objectMapper.readValue(massage, UserRequest.class);
            this.authorizationHeader = signInRequest.getAuthorizationHeader();
        } catch (Exception e){
            System.err.println(massage);
            throw new IllegalArgumentException("Invalid JSON format for UserRequest", e);
        }
    }


    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing UserRequest", e);
        }
    }
}