package com.jay.SocialMedia.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
}
