package com.example.demo_api.model;

import jakarta.persistence.*;
//This class is used for storing getting and setting username, password and Id[from DB]


@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email; // Optional field for email
    private String MobileNumber; // Optional field for phone number

    // ✅ Getter and Setter for ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ✅ Getter and Setter for Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ✅ Getter and Setter for Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ✅ Getter and Setter for Password
    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String phone) {
        this.MobileNumber = phone;
    }
}
