package com.endgame.endgameapi.dto;

public record AuthResponse(
        String token,
        String username,
        String role
) {}