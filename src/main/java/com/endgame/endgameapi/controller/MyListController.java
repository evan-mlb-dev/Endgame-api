package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-list")
@CrossOrigin(origins = "http://localhost:4200")
public class MyListController {
    private final GameRepository gameRepository;
    public MyListController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping("/add")
    public Game addToPlayList(@RequestBody Game game) {
        System.out.println("Nom reçu : " + game.getName());
        return gameRepository.save(game);
    }

    @GetMapping
    public List<Game> getMyList() {
        return gameRepository.findAll();
    }
}