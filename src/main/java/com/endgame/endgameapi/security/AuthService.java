package com.endgame.endgameapi.security;

import com.endgame.endgameapi.dto.AuthResponse;
import com.endgame.endgameapi.model.Role;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client.id}")
    private String googleClientId;

    public AuthResponse loginWithGoogle(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new IllegalArgumentException("Token Google invalide");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUserFromGoogle(email, name));


        String jwt = jwtService.generateToken(user);

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