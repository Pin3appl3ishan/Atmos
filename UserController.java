package com.example.atmos.controller;

import com.example.atmos.model.User;
import com.example.atmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> optionalUser = userService.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (userService.checkPassword(existingUser, user.getPassword())) {
                return ResponseEntity.ok("Login successful!");
            }
        }
        return ResponseEntity.status(401).body("Invalid email or password!");
    }
}
