package com.endgame.endgameapi.security;

import com.endgame.endgameapi.dto.AuthResponse;
import com.endgame.endgameapi.dto.RegisterRequest;
import com.endgame.endgameapi.model.Role;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${google.client.id}")
    private String googleClientId;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("User name already taken !");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already taken !");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), "ROLE_USER");
    }

    public AuthResponse login(RegisterRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            String token = jwtService.generateToken(user);
            return new AuthResponse(token, user.getUsername(), "ROLE_USER");
        } else {
            throw new IllegalStateException("Authentication principal is not valid");
        }
    }

    public AuthResponse loginWithGoogle(String idTokenString) throws Exception {
        log.info("Initiating Google ID token verification...");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            log.error("Error occurred while verifying Google ID token: {}", e.getMessage(), e);
            throw e;
        }

        if (idToken == null) {
            log.warn("Google ID token verification failed: Token is invalid or expired. Expected Client ID: {}", googleClientId);
            throw new IllegalArgumentException("Token Google invalide");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        log.info("Google ID token successfully verified for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("No existing user found for email: {}. Creating a new account...", email);
                    return createUserFromGoogle(email, name);
                });

        log.info("Generating internal JWT for user: {}", user.getUsername());
        String jwt = jwtService.generateToken(user);

        log.info("Google authentication successful for user: {}", user.getUsername());
        return new AuthResponse(jwt, user.getUsername(), "ROLE_USER");
    }

    private User createUserFromGoogle(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(name != null ? name : email.split("@")[0]);
        newUser.setRole(Role.USER);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        return userRepository.save(newUser);
    }
}