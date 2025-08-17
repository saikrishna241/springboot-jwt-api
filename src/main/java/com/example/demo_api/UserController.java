package com.example.demo_api;

import com.example.demo_api.model.User;
import com.example.demo_api.model.UserEntity;
import com.example.demo_api.repository.UserRepository;
import com.example.demo_api.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    // ✅ LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        Optional<UserEntity> userFromDb = userRepo.findByUsername(user.getUsername());
        UserEntity entity;
        if (userFromDb.isPresent()) {
            // Existing user: check password
            entity = userFromDb.get();
            if (!entity.getPassword().equals(user.getPassword())) {
                response.put("status", "fail");
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            // New user: create with all fields from payload
            entity = new UserEntity();
            entity.setUsername(user.getUsername());
            entity.setPassword(user.getPassword());
            entity.setEmail(user.getEmail());
            entity.setMobileNumber(user.getMobileNumber());
            userRepo.save(entity);
        }

        String token = JwtUtil.generateToken(entity.getUsername());
        response.put("status", "success");
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("username", entity.getUsername());
        response.put("email", entity.getEmail());
        response.put("phone", entity.getMobileNumber());

        return ResponseEntity.ok(response);
    }

    // ✅ PROFILE ENDPOINT (JWT Secured)
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getProfile(@RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("error", "Authorization header missing or malformed");
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");

            if (!JwtUtil.validateToken(token)) {
                response.put("error", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String username = JwtUtil.extractUsername(token);
            Optional<UserEntity> user = userRepo.findByUsername(username);

            if (user.isEmpty()) {
                response.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            response.put("username", user.get().getUsername());
            response.put("role", "admin"); // Optional: Add roles in DB later

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Token processing failed");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
