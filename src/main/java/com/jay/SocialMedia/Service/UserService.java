package com.jay.SocialMedia.Service;

import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Entity.User;
import com.jay.SocialMedia.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO saveUser(UserDTO userDTO) {

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getName(),
                savedUser.getEmail()
        );
    }


    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }
}
