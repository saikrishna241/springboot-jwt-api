package com.example.demo_api;

import com.example.demo_api.model.User;
import com.example.demo_api.model.UserEntity;
import com.example.demo_api.repository.UserRepository;
import com.example.demo_api.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    @Operation(
            summary = "Login with username and password",
            description = "Returns a JWT token if credentials are valid",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{ \"username\": \"admin\", \"password\": \"pass\" }")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        Optional<UserEntity> userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb.isPresent() && userFromDb.get().getPassword().equals(user.getPassword())) {
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

    // ✅ PROFILE ENDPOINT (JWT Secured)
    @GetMapping("/profile")
    @Operation(
            summary = "Get user profile (secured)",
            description = "Returns profile details if JWT is valid",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Bearer <JWT>",
                            required = true,
                            example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile returned"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - token invalid or expired"),
                    @ApiResponse(responseCode = "400", description = "Bad request - missing or malformed header")
            }
    )
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
