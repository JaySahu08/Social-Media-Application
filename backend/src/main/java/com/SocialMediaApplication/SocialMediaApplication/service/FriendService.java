package com.SocialMediaApplication.SocialMediaApplication.service;

import com.SocialMediaApplication.SocialMediaApplication.dto.response.FriendRequestDto;
import com.SocialMediaApplication.SocialMediaApplication.dto.response.UserDto;
import com.SocialMediaApplication.SocialMediaApplication.entity.FriendRequest;
import com.SocialMediaApplication.SocialMediaApplication.entity.FriendRequestStatus;
import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import com.SocialMediaApplication.SocialMediaApplication.exception.BadRequestException;
import com.SocialMediaApplication.SocialMediaApplication.exception.ResourceNotFoundException;
import com.SocialMediaApplication.SocialMediaApplication.repository.FriendRequestRepository;
import com.SocialMediaApplication.SocialMediaApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public void sendRequest(String senderUsername, Long receiverId) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new BadRequestException("You cannot add yourself");
        }
        if (sender.getFriends().contains(receiver)) {
            throw new BadRequestException("Already friends");
        }
        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(
                sender, receiver, FriendRequestStatus.PENDING)) {
            throw new BadRequestException("Friend request already sent");
        }

        friendRequestRepository.save(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build());
    }

    public void acceptRequest(Long requestId, String receiverUsername) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FriendRequest req = friendRequestRepository.findByIdAndReceiver(requestId, receiver)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (req.getStatus() != FriendRequestStatus.PENDING) {
            throw new BadRequestException("Request already processed");
        }

        req.setStatus(FriendRequestStatus.ACCEPTED);

        User sender = req.getSender();
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public void rejectRequest(Long requestId, String receiverUsername) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FriendRequest req = friendRequestRepository.findByIdAndReceiver(requestId, receiver)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (req.getStatus() != FriendRequestStatus.PENDING) {
            throw new BadRequestException("Request already processed");
        }

        req.setStatus(FriendRequestStatus.REJECTED);
    }

    public List<FriendRequestDto> getPendingRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return friendRequestRepository
                .findByReceiverAndStatus(user, FriendRequestStatus.PENDING)
                .stream()
                .map(FriendRequestDto::from)
                .toList();
    }

    public List<UserDto> getFriends(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getFriends().stream().map(UserDto::from).toList();
    }
}