package com.eshoppingzone.UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    @NotBlank(message = "Username cannot be blank")  // Ensures the username is not blank
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")  // Ensures the username length is between 3 and 50
    private String username;

    @NotBlank(message = "Password cannot be blank")  // Ensures the password is not blank
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")  // Ensures the password length is between 6 and 100
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
