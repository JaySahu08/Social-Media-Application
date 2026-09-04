package com.jay.SocialMedia;

import com.jay.SocialMedia.DTO.PostDTO;
import com.jay.SocialMedia.Entity.User;
import com.jay.SocialMedia.Repository.ChatMessageRepository;
import com.jay.SocialMedia.Repository.PostCommentRepository;
import com.jay.SocialMedia.Repository.PostLikeRepository;
import com.jay.SocialMedia.Repository.PostRepository;
import com.jay.SocialMedia.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SoicalMediaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();
        postCommentRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createUserListUserAndFetchById() throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of(
                "name", "Jay",
                "email", "jay@example.com",
                "password", "secret123"
        ));

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jay"))
                .andExpect(jsonPath("$.email").value("jay@example.com"))
                .andReturn();

        User createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        Long createdUserId = createdUser.getId();
        assertEquals("http://localhost/users/" + createdUserId, result.getResponse().getHeader("Location"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jay"))
                .andExpect(jsonPath("$[0].email").value("jay@example.com"));

        mockMvc.perform(get("/users/{id}", createdUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jay"))
                .andExpect(jsonPath("$.email").value("jay@example.com"));
    }

    @Test
    void loginUpdateAndDeleteFlowWorks() throws Exception {
        User user = new User("Ava", "ava@example.com", passwordEncoder.encode("secret123"));
        user = userRepository.save(user);

        String loginPayload = objectMapper.writeValueAsString(Map.of(
                "email", "ava@example.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value("Ava"))
                .andExpect(jsonPath("$.email").value("ava@example.com"));

        String updatePayload = objectMapper.writeValueAsString(Map.of(
                "name", "Ava Stone",
                "email", "ava.stone@example.com"
        ));

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ava Stone"))
                .andExpect(jsonPath("$.email").value("ava.stone@example.com"));

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id " + user.getId()));
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        userRepository.save(new User("Ava", "ava@example.com", passwordEncoder.encode("secret123")));

        String payload = objectMapper.writeValueAsString(Map.of(
                "name", "Another Ava",
                "email", "AVA@example.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already registered"));
    }

    @Test
    void postCommentAndLikeFlowWorks() throws Exception {
        User author = userRepository.save(new User("Ava", "ava@example.com", passwordEncoder.encode("secret123")));
        User friend = userRepository.save(new User("Ben", "ben@example.com", passwordEncoder.encode("secret123")));

        String postPayload = objectMapper.writeValueAsString(Map.of(
                "content", "Hello from the API",
                "userId", author.getId()
        ));

        MvcResult createPostResult = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello from the API"))
                .andExpect(jsonPath("$.authorName").value("Ava"))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andReturn();

        PostDTO post = objectMapper.readValue(createPostResult.getResponse().getContentAsString(), PostDTO.class);

        String likePayload = objectMapper.writeValueAsString(Map.of("userId", friend.getId()));

        mockMvc.perform(post("/api/posts/{postId}/like", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(likePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(1));

        mockMvc.perform(post("/api/posts/{postId}/like", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(likePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(0));

        String commentPayload = objectMapper.writeValueAsString(Map.of(
                "text", "Nice post",
                "userId", friend.getId()
        ));

        mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].text").value("Nice post"))
                .andExpect(jsonPath("$.comments[0].authorName").value("Ben"));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(post.getId()))
                .andExpect(jsonPath("$[0].comments", hasSize(1)));
    }

    @Test
    void chatFlowWorksInBothDirections() throws Exception {
        User ava = userRepository.save(new User("Ava", "ava@example.com", passwordEncoder.encode("secret123")));
        User ben = userRepository.save(new User("Ben", "ben@example.com", passwordEncoder.encode("secret123")));

        String firstMessagePayload = objectMapper.writeValueAsString(Map.of(
                "text", "Hi Ben",
                "senderId", ava.getId()
        ));

        mockMvc.perform(post("/api/chats/{friendId}/messages", ben.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstMessagePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Hi Ben"))
                .andExpect(jsonPath("$.senderId").value(ava.getId()))
                .andExpect(jsonPath("$.receiverId").value(ben.getId()));

        String secondMessagePayload = objectMapper.writeValueAsString(Map.of(
                "text", "Hi Ava",
                "senderId", ben.getId()
        ));

        mockMvc.perform(post("/api/chats/{friendId}/messages", ava.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondMessagePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Hi Ava"))
                .andExpect(jsonPath("$.senderId").value(ben.getId()))
                .andExpect(jsonPath("$.receiverId").value(ava.getId()));

        mockMvc.perform(get("/api/chats/{friendId}/messages", ben.getId())
                        .param("currentUserId", ava.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text").value("Hi Ben"))
                .andExpect(jsonPath("$[1].text").value("Hi Ava"));
    }

    @Test
    void invalidPayloadAndBadLoginReturnStructuredErrors() throws Exception {
        String invalidCreatePayload = objectMapper.writeValueAsString(Map.of(
                "name", "",
                "email", "bad-email",
                "password", "123"
        ));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCreatePayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.name").value("Name cannot be empty"))
                .andExpect(jsonPath("$.validationErrors.email").value("Invalid email format"))
                .andExpect(jsonPath("$.validationErrors.password").value("Password must be at least 6 characters long"));

        userRepository.save(new User("Ava", "ava@example.com", passwordEncoder.encode("secret123")));

        String invalidLoginPayload = objectMapper.writeValueAsString(Map.of(
                "email", "ava@example.com",
                "password", "wrong-password"
        ));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLoginPayload))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}
