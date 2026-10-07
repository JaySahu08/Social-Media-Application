package com.SocialMediaApplication.SocialMediaApplication.controller;

import com.SocialMediaApplication.SocialMediaApplication.dto.response.FriendRequestDto;
import com.SocialMediaApplication.SocialMediaApplication.dto.response.UserDto;
import com.SocialMediaApplication.SocialMediaApplication.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{receiverId}")
    public ResponseEntity<Map<String, String>> sendRequest(@PathVariable Long receiverId,
                                                           Authentication auth) {
        friendService.sendRequest(auth.getName(), receiverId);
        return ResponseEntity.ok(Map.of("message", "Friend request sent"));
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Map<String, String>> accept(@PathVariable Long requestId,
                                                      Authentication auth) {
        friendService.acceptRequest(requestId, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Friend request accepted"));
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<Map<String, String>> reject(@PathVariable Long requestId,
                                                      Authentication auth) {
        friendService.rejectRequest(requestId, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Friend request rejected"));
    }

    @GetMapping("/requests/pending")
    public List<FriendRequestDto> pending(Authentication auth) {
        return friendService.getPendingRequests(auth.getName());
    }

    @GetMapping
    public List<UserDto> myFriends(Authentication auth) {
        return friendService.getFriends(auth.getName());
    }
}