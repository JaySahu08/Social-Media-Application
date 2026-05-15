package com.jay.SocialMedia.Repository;

import com.jay.SocialMedia.Entity.Post;
import com.jay.SocialMedia.Entity.PostLike;
import com.jay.SocialMedia.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}