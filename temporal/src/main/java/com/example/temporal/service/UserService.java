package com.example.temporal.service;

import com.example.temporal.model.User;
import com.example.temporal.repository.UserRepository;
import io.jsonwebtoken.Claims;
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


    public Optional<User> getUserByUid(UUID uid) {
        return userRepository.findByUid(uid);
    }



    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<User> getUserFromToken(String token) {
        Claims claims = decodeToken(token);
        String email = claims.getSubject();
        String uid = claims.get("uid", String.class);

        if (email != null && uid != null) {
            return getUserByUid(UUID.fromString(uid));
        }
        return Optional.empty();
    }
}
