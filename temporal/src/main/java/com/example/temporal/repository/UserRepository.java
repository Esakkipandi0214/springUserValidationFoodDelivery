package com.example.temporal.repository;

import com.example.temporal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUid(UUID uid);

    // Method to find a user by their email address
    Optional<User> findByEmail(String email);
}
