package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/User")
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @PostMapping("/users")
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PutMapping("/user")
    public UserDTO UpdateUser(@RequestBody @Valid UserDTO userDTO){
        return userService.updateUser(userDTO);
    }
}
