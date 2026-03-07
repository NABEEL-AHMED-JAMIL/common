package com.barco.common.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtSubject {

    private String uuid;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public JwtSubject() {}

    public String getUuid() {
        return uuid;
    }

    public JwtSubject setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public JwtSubject setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public JwtSubject setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public JwtSubject setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public JwtSubject setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
