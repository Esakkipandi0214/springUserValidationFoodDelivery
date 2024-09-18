package com.example.temporal.controller;

import com.example.temporal.model.User;
import com.example.temporal.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Decode JWT token and validate user
    @GetMapping("/token/decode")
    public ResponseEntity<String> decodeToken(@RequestParam String token) {
        try {
            Claims claims = userService.decodeToken(token);
            String email = claims.getSubject();
            String uid = claims.get("uid", String.class);

            Optional<User> user = userService.getUserByUid(UUID.fromString(uid));
            if (user.isPresent() && user.get().getEmail().equals(email)) {
                return ResponseEntity.ok("yes");
            } else {
                return ResponseEntity.ok("no");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("no");
        }
    }
}
