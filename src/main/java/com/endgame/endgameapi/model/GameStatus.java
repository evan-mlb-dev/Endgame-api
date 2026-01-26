package com.endgame.endgameapi.model;

import lombok.Getter;

@Getter
public enum GameStatus {
    PLAN_TO_PLAY("Plan to Play"),
    PLAYING("Playing"),
    COMPLETED("Completed"),
    DROPPED("Dropped"),
    ON_HOLD("On Hold");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }
}