package com.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dtos.LoginRequest;
import com.api.dtos.RegisterRequest;
import com.api.dtos.UserResponse;
import com.api.entities.PinSession;
import com.api.entities.User;
import com.api.services.AuthService;
import com.api.services.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControler {

    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(UserResponse.fromUser(authService.loadUserByUsername(userDetails.getUsername())));
    }

    @PostMapping("/register")
    public ResponseEntity<String> newRegisterUser(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Confirm pin at /register/pin");
    }

    @PostMapping("/register/pin")
    public ResponseEntity<User> verifyPin(@RequestBody PinSession request) {
        return ResponseEntity.ok(authService.verifyUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        User user = authService.authenticateUser(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }
}
