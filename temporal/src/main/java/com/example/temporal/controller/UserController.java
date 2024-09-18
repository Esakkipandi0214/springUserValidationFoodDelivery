package com.example.temporal.controller;

import com.example.temporal.model.User;
import com.example.temporal.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Read a user by UUID
    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable("uid") String uid) {
        Optional<User> user = userService.getUserByUid(UUID.fromString(uid));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a user by UUID
    @PutMapping("/{uid}")
    public ResponseEntity<User> updateUser(@PathVariable("uid") String uid, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(UUID.fromString(uid), user);
        return updatedUser != null ? new ResponseEntity<>(updatedUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a user by UUID
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable("uid") String uid) {
        boolean deleted = userService.deleteUser(UUID.fromString(uid));
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        String token = userService.authenticateUser(email, password);
        if (token != null) {
            return ResponseEntity.ok().body(token);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
