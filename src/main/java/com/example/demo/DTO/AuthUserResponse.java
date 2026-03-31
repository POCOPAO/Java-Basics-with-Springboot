package com.example.demo.DTO;

import java.util.List;

public class AuthUserResponse {

    private Long libraryUserId;
    private String username;
    private List<String> roles;

    public AuthUserResponse() {
    }

    public AuthUserResponse(Long libraryUserId, String username, List<String> roles) {
        this.libraryUserId = libraryUserId;
        this.username = username;
        this.roles = roles;
    }

    public Long getLibraryUserId() {
        return libraryUserId;
    }

    public void setLibraryUserId(Long libraryUserId) {
        this.libraryUserId = libraryUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
