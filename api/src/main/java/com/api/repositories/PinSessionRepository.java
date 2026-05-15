package com.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.PinSession;

@Repository
public interface PinSessionRepository extends JpaRepository<PinSession, Long> {

    Optional<PinSession> findByEmailAndPin(String email, String pin);

    Optional<PinSession> findByEmail(String email);
}
