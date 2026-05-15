package com.jay.SocialMedia;

import com.jay.SocialMedia.Entity.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class SoicalMediaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
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
                "email", "ava@example.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already registered"));
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
