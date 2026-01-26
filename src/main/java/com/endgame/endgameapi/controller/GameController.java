package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PatchMapping("/change/status/{id}")
    public ResponseEntity<Game> changeStatus(@PathVariable Long id, @RequestBody GameStatus newStatus) {
        return gameRepository.findById(id).map(game -> {
            game.setStatus(newStatus);
            gameRepository.save(game);
            return ResponseEntity.ok(game);
        }).orElse(ResponseEntity.notFound().build());
    }


}