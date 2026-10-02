package com.SocialMediaApplication.SocialMediaApplication.dto;

import com.SocialMediaApplication.SocialMediaApplication.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private LocalDateTime sentAt;

    public static ChatMessageDto from(Message m) {
        return new ChatMessageDto(
                m.getId(),
                m.getSender().getUsername(),
                m.getReceiver().getUsername(),
                m.getContent(),
                m.getSentAt()
        );
    }
}