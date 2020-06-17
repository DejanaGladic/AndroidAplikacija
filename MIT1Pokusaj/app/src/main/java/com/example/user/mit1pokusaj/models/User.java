package com.example.user.mit1pokusaj.models;

public class User {
    public String fullName,userName,email;

    public User(){

    }

    public User(String fullName, String userName, String email) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
