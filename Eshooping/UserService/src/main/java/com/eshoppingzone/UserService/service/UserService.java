package com.eshoppingzone.UserService.service;

import com.eshoppingzone.UserService.dto.AuthRequest;
import com.eshoppingzone.UserService.dto.UserDTO;
import com.eshoppingzone.UserService.model.User;
import com.eshoppingzone.UserService.repository.UserRepository;
import com.eshoppingzone.UserService.util.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;



    public String login(AuthRequest authRequest) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            return jwtUtil.generateToken(authRequest.getUsername());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }



    public ResponseEntity<?> validateToken(String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
        }

        String email = jwtUtil.extractUsername(token);
        User user = userRepo.findByUsername(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        return userRepo.save(user);
    }


    public Optional<User> findByUsername(String username) {

        return userRepo.findByUsername(username);
    }

//    public void generateResetToken(String email) {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String token = UUID.randomUUID().toString();
//        user.setResetToken(token);
//        user.setTokenExpirationTime(LocalDateTime.now().plusMinutes(15));
//        userRepo.save(user);
//
//        // Mock email (can integrate JavaMailSender here)
//        System.out.println("Reset link: http://localhost:4200/reset-password?token=" + token);
//    }

//    public ResponseEntity<User> resetPassword(String token, String newPassword) {
//        Optional<User> optionalUser = userRepo.findByResetToken(token);
//
//        if (optionalUser.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//        User user = optionalUser.get();
//
//        // Optionally validate token expiry
//        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//System.out.println(user);
//        // Hash the password before saving (RECOMMENDED)
//        user.setPassword(passwordEncoder.encode(newPassword));
//
//        System.out.println("Password reset for username: " + user.getUsername());
//
//        // Clear token after successful reset
//        user.setResetToken(null);
//        user.setTokenExpirationTime(null);
//
//        userRepo.save(user); // <-- This is critical!
//
//        return ResponseEntity.ok(user);
//    }
}
