package com.endgame.endgameapi.dto;

import com.endgame.endgameapi.model.GameStatus;
import org.antlr.v4.runtime.misc.NotNull;


public record UserGameDTO(
        @NotNull
        Long userId,
        @NotNull
        Long gameId,
        @NotNull
        GameStatus status
) {
}