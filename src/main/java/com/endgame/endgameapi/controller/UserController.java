package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.dto.RegisterRequest;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestBody RegisterRequest request,
                                            @AuthenticationPrincipal User currentUser) {
        try {
            User updatedUser = userService.updateUsername(currentUser.getId(), request.username());
            return ResponseEntity.ok(Map.of(
                    "message", "Updated username successfully.",
                    "username", updatedUser.getUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}