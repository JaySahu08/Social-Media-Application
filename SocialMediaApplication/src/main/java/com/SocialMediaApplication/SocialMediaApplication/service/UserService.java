package com.SocialMediaApplication.SocialMediaApplication.service;

import com.SocialMediaApplication.SocialMediaApplication.dto.response.UserDto;
import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import com.SocialMediaApplication.SocialMediaApplication.exception.ResourceNotFoundException;
import com.SocialMediaApplication.SocialMediaApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> searchUsers(String query, String currentUsername) {
        return userRepository.searchUsers(query, currentUsername)
                .stream()
                .map(UserDto::from)
                .toList();
    }

    public UserDto getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDto.from(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}