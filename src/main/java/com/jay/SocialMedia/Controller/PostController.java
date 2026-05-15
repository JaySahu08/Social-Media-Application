package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.AddCommentRequest;
import com.jay.SocialMedia.DTO.CreatePostRequest;
import com.jay.SocialMedia.DTO.LikePostRequest;
import com.jay.SocialMedia.DTO.PostDTO;
import com.jay.SocialMedia.Service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public List<PostDTO> getPosts() {
        return postService.getPosts();
    }

    @PostMapping("/api/posts")
    public PostDTO createPost(@RequestBody @Valid CreatePostRequest request) {
        return postService.createPost(request);
    }

    @PostMapping("/api/posts/{postId}/like")
    public PostDTO likePost(
            @PathVariable Long postId,
            @RequestBody @Valid LikePostRequest request
    ) {
        return postService.likePost(postId, request.getUserId());
    }

    @PostMapping("/api/posts/{postId}/comments")
    public PostDTO addComment(
            @PathVariable Long postId,
            @RequestBody @Valid AddCommentRequest request
    ) {
        return postService.addComment(postId, request);
    }
}