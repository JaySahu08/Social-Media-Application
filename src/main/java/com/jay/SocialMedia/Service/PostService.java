package com.jay.SocialMedia.Service;

import com.jay.SocialMedia.DTO.AddCommentRequest;
import com.jay.SocialMedia.DTO.CommentDTO;
import com.jay.SocialMedia.DTO.CreatePostRequest;
import com.jay.SocialMedia.DTO.PostDTO;
import com.jay.SocialMedia.Entity.Post;
import com.jay.SocialMedia.Entity.PostComment;
import com.jay.SocialMedia.Entity.PostLike;
import com.jay.SocialMedia.Entity.User;
import com.jay.SocialMedia.Exception.ResourceNotFoundException;
import com.jay.SocialMedia.Repository.PostCommentRepository;
import com.jay.SocialMedia.Repository.PostLikeRepository;
import com.jay.SocialMedia.Repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserService userService;

    public PostService(
            PostRepository postRepository,
            PostCommentRepository postCommentRepository,
            PostLikeRepository postLikeRepository,
            UserService userService
    ) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postLikeRepository = postLikeRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public PostDTO createPost(CreatePostRequest request) {
        User author = userService.getExistingUser(request.getUserId());

        Post post = new Post();
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setVideoUrl(request.getVideoUrl());
        post.setAuthor(author);

        return toDto(postRepository.save(post));
    }

    @Transactional
    public PostDTO likePost(Long postId, Long userId) {
        Post post = getExistingPost(postId);
        User user = userService.getExistingUser(userId);

        postLikeRepository.findByPostAndUser(post, user)
                .ifPresentOrElse(
                        postLikeRepository::delete,
                        () -> {
                            PostLike like = new PostLike();
                            like.setPost(post);
                            like.setUser(user);
                            postLikeRepository.save(like);
                        }
                );

        return toDto(getExistingPost(postId));
    }

    @Transactional
    public PostDTO addComment(Long postId, AddCommentRequest request) {
        Post post = getExistingPost(postId);
        User author = userService.getExistingUser(request.getUserId());

        PostComment comment = new PostComment();
        comment.setText(request.getText());
        comment.setPost(post);
        comment.setAuthor(author);

        postCommentRepository.save(comment);

        return toDto(getExistingPost(postId));
    }

    private Post getExistingPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
    }

    private PostDTO toDto(Post post) {
        List<CommentDTO> comments = post.getComments()
                .stream()
                .map(this::toCommentDto)
                .toList();

        return new PostDTO(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getVideoUrl(),
                post.getAuthor().getId(),
                post.getAuthor().getName(),
                post.getCreatedAt(),
                post.getLikes().size(),
                comments
        );
    }

    private CommentDTO toCommentDto(PostComment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreatedAt()
        );
    }
}
