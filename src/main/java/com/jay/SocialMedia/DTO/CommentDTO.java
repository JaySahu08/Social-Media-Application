package com.jay.SocialMedia.DTO;

import java.time.Instant;

public class CommentDTO {

    private Long id;
    private String text;
    private Long authorId;
    private String authorName;
    private Instant createdAt;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, Long authorId, String authorName, Instant createdAt) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}