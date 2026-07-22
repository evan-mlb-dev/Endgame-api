package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.dto.AuthResponse;
import com.endgame.endgameapi.dto.RegisterRequest;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // 1. Check user exist
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "User name already taken !"));
        }
        else if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Email already taken !"));
        }

        // 2. create new user
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return ResponseEntity.ok().body(Map.of("message","User created succesfully." )) ;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Object principal = authentication.getPrincipal();

            if (principal instanceof User user) {
                return ResponseEntity.ok(new AuthResponse(user.getUsername(), "ROLE_USER"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de la récupération du profil utilisateur");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid logins");
        }
    }
}
