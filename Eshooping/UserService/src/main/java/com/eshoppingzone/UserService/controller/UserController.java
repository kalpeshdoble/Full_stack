package com.eshoppingzone.UserService.controller;

import com.eshoppingzone.UserService.dto.AuthRequest;
import com.eshoppingzone.UserService.dto.AuthResponse;
import com.eshoppingzone.UserService.dto.UserDTO;
import com.eshoppingzone.UserService.exception.ResourceNotFoundException;
import com.eshoppingzone.UserService.model.User;
import com.eshoppingzone.UserService.repository.UserRepository;
import com.eshoppingzone.UserService.service.UserService;
import com.eshoppingzone.UserService.util.TokenBlacklistService;
import com.eshoppingzone.UserService.util.jwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private jwtUtil jwtUtil;



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

//    @PostMapping("/register")
//    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
//
//        User savedUser = userService.registerUser(userDTO);
//        return ResponseEntity.ok(savedUser);
//    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {
        // Conversion to entity and logic here
        userDTO.setRole("CUSTOMER");
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            String token = userService.login(authRequest);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/role/{username}")
    public ResponseEntity<String> getUserRole(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(value -> ResponseEntity.ok(value.getRole()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("ROLE_NOT_FOUND"));
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        return userService.validateToken(tokenHeader);
    }
@Autowired
private UserRepository userRepository;

    @GetMapping("/users/role/{email}")
    public ResponseEntity<String> getUserRoleByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        return ResponseEntity.ok(user.getRole());

    }
    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {
        String username = authentication.getName();

        Optional<User> userOpt = userRepository.findByUsername(username);

        return userOpt.orElseThrow(() -> new RuntimeException("User not found"));
    }
    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String resetToken = jwtUtil.generateToken(username);  // Or generate random token
        // TODO: Send resetToken via email here

        return ResponseEntity.ok("Password reset token: " + resetToken);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        String username = jwtUtil.extractUsername(token);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logout successful. Token is invalidated.");
        }
        return ResponseEntity.badRequest().body("Authorization header is missing or invalid.");
    }

}
