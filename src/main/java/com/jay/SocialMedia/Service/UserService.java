package com.jay.SocialMedia.Service;

import com.jay.SocialMedia.DTO.CreateUserRequest;
import com.jay.SocialMedia.DTO.LoginRequest;
import com.jay.SocialMedia.DTO.UpdateUserRequest;
import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Entity.User;
import com.jay.SocialMedia.Exception.DuplicateEmailException;
import com.jay.SocialMedia.Exception.InvalidCredentialsException;
import com.jay.SocialMedia.Exception.ResourceNotFoundException;
import com.jay.SocialMedia.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveUser(CreateUserRequest request) {
        ensureEmailIsAvailable(request.getEmail(), null);

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return toDto(userRepository.save(user));
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getFriends(Long userId) {
        User currentUser = getExistingUser(userId);

        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        return toDto(getExistingUser(id));
    }

    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = getExistingUser(id);
        ensureEmailIsAvailable(request.getEmail(), id);

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.delete(getExistingUser(id));
    }

    public UserDTO login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return toDto(user);
    }

    public User getExistingUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    private void ensureEmailIsAvailable(String email, Long currentUserId) {
        userRepository.findByEmail(email)
                .filter(existingUser -> !existingUser.getId().equals(currentUserId))
                .ifPresent(existingUser -> {
                    throw new DuplicateEmailException("Email is already registered");
                });
    }

    public UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
