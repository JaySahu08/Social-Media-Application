package com.jay.SocialMedia.Repository;

import com.jay.SocialMedia.Entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}