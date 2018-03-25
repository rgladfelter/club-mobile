package com.radford.clubmobile.models;

public class RegistrationRequest {
    final String username;
    final String password;
    final String firstName;
    final String lastName;


    public RegistrationRequest(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
