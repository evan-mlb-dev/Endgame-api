package com.endgame.endgameapi.service;


import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public boolean exist(Long gameId) {
        return this.gameRepository.existsById(gameId);
    }
}
