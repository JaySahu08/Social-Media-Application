package com.SocialMediaApplication.SocialMediaApplication.dto.response;

import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String profilePicture;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getProfilePicture()
        );
    }
}