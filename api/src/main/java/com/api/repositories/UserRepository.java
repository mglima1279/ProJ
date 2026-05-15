package com.api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.entities.User;
import com.api.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
                SELECT u FROM User u
                WHERE u.role = :role
            """)
    Page<User> findAllByRole(@Param("role") Role role, Pageable pageable);

    Optional<User> findByEmail(String email);
}