package com.jay.SocialMedia.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddCommentRequest {

    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 300, message = "Comment cannot be longer than 300 characters")
    private String text;

    @NotNull(message = "User id is required")
    private Long userId;

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}