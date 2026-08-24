package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.dto.AuthResponse;
import com.endgame.endgameapi.dto.ErrorResponse;
import com.endgame.endgameapi.dto.RegisterRequest;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.repository.UserRepository;
import com.endgame.endgameapi.security.JwtService;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // 1. Check user exist
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "User name already taken !"));
        } else if (userRepository.findByEmail(request.email()).isPresent()) {
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

        // 3. Generate token & return AuthResponse (identical to /login)
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "ROLE_USER"));
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
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "ROLE_USER"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Server Error."));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Bad logins."));
        }
    }
}
