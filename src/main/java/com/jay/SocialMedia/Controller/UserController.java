package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.CreateUserRequest;
import com.jay.SocialMedia.DTO.LoginRequest;
import com.jay.SocialMedia.DTO.UpdateUserRequest;
import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping({"", "/api"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Social media backend is running";
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserDTO createdUser = userService.saveUser(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<UserDTO> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody @Valid LoginRequest request) {
        return userService.login(request);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }
}
