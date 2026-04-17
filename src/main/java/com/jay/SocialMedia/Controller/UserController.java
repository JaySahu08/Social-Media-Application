package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/hello")
    public String HelloWorld(){
        return "hello world again again!!";
    }

    @GetMapping(path = "/users")
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO She = userService.saveUser(userDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(She.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody @Valid UserDTO userDTO){
        UserDTO user = userService.login(userDTO);
        return "Login Succesfull";
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PutMapping("/users")
    public UserDTO UpdateUser(@RequestBody @Valid UserDTO userDTO){
        return userService.updateUser(userDTO);
    }
}
