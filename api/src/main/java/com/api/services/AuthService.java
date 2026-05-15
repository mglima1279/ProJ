package com.api.services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.dtos.LoginRequest;
import com.api.dtos.RegisterRequest;
import com.api.entities.PinSession;
import com.api.entities.User;
import com.api.exceptions.CustomException;
import com.api.repositories.PinSessionRepository;
import com.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PinSessionRepository pinSessionRepository;

    private final Random random = new Random();

    @Override
    public User loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found with email: " + email));

        return user;
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()
                || pinSessionRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Email already exists on sys: " + request.getEmail());
        }

        User user = request.toUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String pin = String.valueOf(random.nextInt(100000, 1000000));
        PinSession pinEntity = new PinSession();
        pinEntity.setEmail(request.getEmail());
        pinEntity.setPin(pin);
        pinEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        pinSessionRepository.save(pinEntity);

        emailService.sendEmail(request.getEmail(), "VERIFY PIN", pin);
    }

    public User verifyUser(PinSession pinSession) {
        if (!isPinValid(pinSession)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED,
                    "Invalid pin {" + pinSession.getPin() + "} and email {" + pinSession.getEmail() + "}");
        }

        User user = userRepository.findByEmail(pinSession.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                "User not fount with email: " + pinSession.getEmail()));
        user.setIsVerified(true);
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

    private boolean isPinValid(PinSession pinSession) {
        PinSession dbPinSession = pinSessionRepository.findByEmailAndPin(pinSession.getEmail(), pinSession.getPin())
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED,
                "Invalid pin {" + pinSession.getPin() + "} and email {" + pinSession.getEmail() + "}"));

        return dbPinSession.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
