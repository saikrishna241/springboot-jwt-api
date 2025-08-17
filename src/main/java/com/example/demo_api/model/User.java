package com.example.demo_api.model;

public class User {


        private String username;
        private String password;
        private String email;
        private String MobileNumber;

    //  Getter for username
    public String getUsername() {
        return username;
    }

    //  Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    //  Getter for password
    public String getPassword() {
        return password;
    }

    //  Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    //  Getter for email
    public String getEmail() {
        return email;
    }

    //  Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    //  Getter for phone
    public String getMobileNumber() {
        return MobileNumber;
    }

    //  Setter for phone
    public void setMobileNumber(String phone) {
        this.MobileNumber = MobileNumber;
    }
}
