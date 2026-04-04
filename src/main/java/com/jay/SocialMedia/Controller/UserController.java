package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/User")
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }
    @PostMapping("/users")
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @GetMapping(path = "/hello")
    public String HelloWorld(){
        return "hello world again again!!";
    }
}
