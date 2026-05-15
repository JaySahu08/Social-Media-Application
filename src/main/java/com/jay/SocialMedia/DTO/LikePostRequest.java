package com.jay.SocialMedia.DTO;

import jakarta.validation.constraints.NotNull;

public class LikePostRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}