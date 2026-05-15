package com.api.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pin_session_tb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PinSession {

    private String email;
    private String pin;
    private LocalDateTime expiresAt;
}
