package com.jay.SocialMedia.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SendMessageRequest {

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 500, message = "Message cannot be longer than 500 characters")
    private String text;

    @NotNull(message = "Sender id is required")
    private Long senderId;

    public String getText() {
        return text;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}