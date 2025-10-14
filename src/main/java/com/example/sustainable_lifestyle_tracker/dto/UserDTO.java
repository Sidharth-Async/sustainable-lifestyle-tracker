package com.example.sustainable_lifestyle_tracker.dto;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private String username;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();

    public UserDTO() {
        // ✅ Automatically assign default role if none is given
        this.roles.add("USER");
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        // ✅ Prevent null or empty roles
        if (roles == null || roles.isEmpty()) {
            this.roles = new HashSet<>();
            this.roles.add("USER");
        } else {
            this.roles = roles;
        }
    }
}
