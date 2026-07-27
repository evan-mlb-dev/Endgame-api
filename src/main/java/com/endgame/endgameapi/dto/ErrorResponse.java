package com.endgame.endgameapi.dto;

public record ErrorResponse(
        int status,
        String message
) {}