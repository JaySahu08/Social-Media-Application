package com.SocialMediaApplication.SocialMediaApplication.dto.response;

import com.SocialMediaApplication.SocialMediaApplication.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String senderFullName;

    public static FriendRequestDto from(FriendRequest fr) {
        return new FriendRequestDto(
                fr.getId(),
                fr.getSender().getId(),
                fr.getSender().getUsername(),
                fr.getSender().getFullName()
        );
    }
}