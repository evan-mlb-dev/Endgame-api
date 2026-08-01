package com.endgame.endgameapi.model;

import lombok.Getter;

@Getter
public enum GameStatus {
    TO_PLAY("TO_PLAY"),
    PLAYING("PLAYING"),
    COMPLETED("COMPLETED"),
    DROPPED("DROPPED"),
    ON_HOLD("ON HOLD");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }
}