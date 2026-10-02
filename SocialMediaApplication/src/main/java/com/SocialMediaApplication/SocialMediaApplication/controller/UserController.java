package com.SocialMediaApplication.SocialMediaApplication.controller;

import com.SocialMediaApplication.SocialMediaApplication.dto.response.UserDto;
import com.SocialMediaApplication.SocialMediaApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String query, Authentication auth) {
        return userService.searchUsers(query, auth.getName());
    }

    @GetMapping("/me")
    public UserDto me(Authentication auth) {
        return userService.getProfile(auth.getName());
    }
}