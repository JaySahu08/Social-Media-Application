package com.jay.SocialMedia.Repository;

import com.jay.SocialMedia.Entity.Post;
import com.jay.SocialMedia.Entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPostOrderByCreatedAtAsc(Post post);
}
