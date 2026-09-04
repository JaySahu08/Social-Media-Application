package com.jay.SocialMedia.Service;

import com.jay.SocialMedia.DTO.ChatMessageDTO;
import com.jay.SocialMedia.DTO.SendMessageRequest;
import com.jay.SocialMedia.Entity.ChatMessage;
import com.jay.SocialMedia.Entity.User;
import com.jay.SocialMedia.Repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatService(ChatMessageRepository chatMessageRepository, UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long currentUserId, Long friendId) {
        User currentUser = userService.getExistingUser(currentUserId);
        User friend = userService.getExistingUser(friendId);

        return chatMessageRepository
                .findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtAsc(
                        currentUser,
                        friend,
                        currentUser,
                        friend
                )
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ChatMessageDTO sendMessage(Long friendId, SendMessageRequest request) {
        User sender = userService.getExistingUser(request.getSenderId());
        User receiver = userService.getExistingUser(friendId);

        ChatMessage message = new ChatMessage();
        message.setText(request.getText());
        message.setSender(sender);
        message.setReceiver(receiver);

        return toDto(chatMessageRepository.save(message));
    }

    private ChatMessageDTO toDto(ChatMessage message) {
        return new ChatMessageDTO(
                message.getId(),
                message.getText(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getReceiver().getId(),
                message.getReceiver().getName(),
                message.getCreatedAt()
        );
    }
}
