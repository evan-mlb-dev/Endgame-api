package com.endgame.endgameapi.service;

import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User updateUsername(Long userId, String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username can't be empty.");
        }

        String trimmedUsername = newUsername.trim();

        userRepository.findByUsername(trimmedUsername).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(userId)) {
                throw new IllegalArgumentException("User name already taken !");
            }
        });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setUsername(trimmedUsername);
        return userRepository.save(user);
    }
}