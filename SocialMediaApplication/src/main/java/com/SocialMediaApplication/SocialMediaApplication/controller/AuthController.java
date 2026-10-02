package com.SocialMediaApplication.SocialMediaApplication.controller;

import com.SocialMediaApplication.SocialMediaApplication.dto.request.LoginRequest;
import com.SocialMediaApplication.SocialMediaApplication.dto.request.RegisterRequest;
import com.SocialMediaApplication.SocialMediaApplication.dto.response.AuthResponse;
import com.SocialMediaApplication.SocialMediaApplication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}