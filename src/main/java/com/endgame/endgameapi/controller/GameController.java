package com.endgame.endgameapi.controller;


import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    @GetMapping
    public List<Game> getGames() {
        return gameRepository.findAll();
    }
}
