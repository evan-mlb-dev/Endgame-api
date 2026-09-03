package com.endgame.endgameapi.service;

import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public boolean exist(Long gameId) {
        return this.gameRepository.existsById(gameId);
    }

    public List<Game> getGameList(List<Long> gameIds) {
        if (gameIds == null || gameIds.isEmpty()) {
            return Collections.emptyList();
        }
        return gameRepository.findByIdIn(gameIds);
    }

    public List<Game> getRandomGames() {
        return gameRepository.find50RandomGames();
    }

    public List<Game> searchGameByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return gameRepository.find50RandomGames();
        }
        return gameRepository.findByNameContainingIgnoreCase(name);
    }
}