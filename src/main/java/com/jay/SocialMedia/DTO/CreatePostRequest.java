package com.jay.SocialMedia.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePostRequest {

    @NotBlank(message = "Post content cannot be empty")
    @Size(max = 500, message = "Post cannot be longer than 500 characters")
    private String content;

    private String imageUrl;

    private String videoUrl;

    @NotNull(message = "User id is required")
    private Long userId;

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}