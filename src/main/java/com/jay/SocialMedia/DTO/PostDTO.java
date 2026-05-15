package com.jay.SocialMedia.DTO;

import java.time.Instant;
import java.util.List;

public class PostDTO {

    private Long id;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private Long authorId;
    private String authorName;
    private Instant createdAt;
    private int likeCount;
    private List<CommentDTO> comments;

    public PostDTO() {
    }

    public PostDTO(
            Long id,
            String content,
            String imageUrl,
            String videoUrl,
            Long authorId,
            String authorName,
            Instant createdAt,
            int likeCount,
            List<CommentDTO> comments
    ) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
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

    public int getLikeCount() {
        return likeCount;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}