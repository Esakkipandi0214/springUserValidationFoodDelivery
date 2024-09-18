package com.example.temporal.service;

import com.example.temporal.model.User;
import com.example.temporal.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String SECRET_KEY = "your-secret-key"; // Use a secure key

    public User registerUser(User user) {
        // Hash the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getUserByUid(UUID uid) {
        return userRepository.findByUid(uid);
    }

    public User updateUser(UUID uid, User user) {
        Optional<User> existingUser = userRepository.findByUid(uid);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            // Update user details
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(updatedUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(UUID uid) {
        Optional<User> existingUser = userRepository.findByUid(uid);
        if (existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
            return true;
        } else {
            return false;
        }
    }

    public String authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return generateToken(userOptional.get());
        } else {
            return null; // Authentication failed
        }
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())  // You can keep this as the subject if it makes sense for your application
                .claim("uid", user.getUid())
                .claim("username", user.getUsername())  // Add username claim
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }}
