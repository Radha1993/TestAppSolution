package com.example.testappsolution.model;

public class User {
    String username,email,password,dob;
    String age;
    String uid;

    public User(String username, String email, String password, String dob, String age, String uid) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.age = age;
        this.uid = uid;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}