package com.quest.etna.model;

public class MeResponse {
    private String username;
    private UserRole role;

    public MeResponse() {
    }

    public MeResponse(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
