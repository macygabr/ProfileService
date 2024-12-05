package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_info")
public class User {
    @Id
    private Long id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private String token;
}