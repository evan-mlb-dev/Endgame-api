package com.endgame.endgameapi.dto;

public record AuthResponse(
        String username,
        String role
) {}