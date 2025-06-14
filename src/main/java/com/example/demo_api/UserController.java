package com.example.demo_api;

import com.example.demo_api.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if ("admin".equals(user.getUsername()) && "pass".equals(user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getProfile(@RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();

        // Remove "Bearer " prefix
        String token = authHeader.replace("Bearer ", "");

        if (!JwtUtil.validateToken(token)) {
            response.put("error", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = JwtUtil.extractUsername(token);
        response.put("username", username);
        response.put("role", "admin"); // Just example data

        return ResponseEntity.ok(response);
    }
}
