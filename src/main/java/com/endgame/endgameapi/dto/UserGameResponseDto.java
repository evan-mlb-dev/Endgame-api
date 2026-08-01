package com.endgame.endgameapi.dto;

import com.endgame.endgameapi.model.GameStatus;

public record UserGameResponseDto(
        String message,
        Long userId,
        Long gameId,
        GameStatus status
) {
}