package com.endgame.endgameapi.controller;


import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<Game>> getByIds(@RequestParam(name = "ids") List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<Game> games = gameRepository.findByIdIn(ids);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/50R")
    public List<Game> get50Randoms() {
        return gameRepository.find50RandomGames();
    }

    @GetMapping("/search")
    public List<Game> searchGame(@RequestParam(required = false) String name) {
        if (name == null || name.trim().isEmpty()) {
            return gameRepository.find50RandomGames();
        }

        return gameRepository.findByNameContainingIgnoreCase(name);
    }

}
