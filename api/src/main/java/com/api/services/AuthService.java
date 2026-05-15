package com.api.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.dtos.LoginRequest;
import com.api.dtos.RegisterRequest;
import com.api.entities.User;
import com.api.exceptions.CustomException;
import com.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found with email: " + email));

        return user;
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Email already exists on sys: " + request.getEmail());
        }

        User user = request.toUser();

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User authenticateUser(LoginRequest request) {
        if (!passwordMatches(request)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return loadUserByUsername(request.getEmail());
    }

    private Boolean passwordMatches(LoginRequest request) {
        String encodedPassword = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Credentials"))
                .getPassword();

        return passwordEncoder.matches(request.getPassword(), encodedPassword);
    }
}
