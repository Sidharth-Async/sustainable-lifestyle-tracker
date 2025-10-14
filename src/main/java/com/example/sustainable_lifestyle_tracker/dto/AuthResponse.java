package com.example.sustainable_lifestyle_tracker.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String displayUsername;

    public AuthResponse(String token, String displayUsername) {
        this.token = token;
        this.displayUsername = displayUsername;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }
    public String getDisplayUsername() {
        return displayUsername;
    }

    public void setDisplayUsername(String displayUsername) {
        this.displayUsername = displayUsername;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
