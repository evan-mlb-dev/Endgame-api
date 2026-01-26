package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-list")
public class MyListController {
    private final GameRepository gameRepository;
    public MyListController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping
    public List<Game> getMyList() {
        return gameRepository.findAll();
    }

    @PostMapping("/add")
    public Game addToPlayList(@RequestBody Game game) {
        return gameRepository.save(game);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeFromList(@PathVariable Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}