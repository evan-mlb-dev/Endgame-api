package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<Game>> getByIds(@RequestParam(name = "ids", required = false) List<Long> ids) {
        List<Game> games = gameService.getGameList(ids);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/50R")
    public ResponseEntity<List<Game>> get50Randoms() {
        return ResponseEntity.ok(gameService.getRandomGames());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGame(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(gameService.searchGameByName(name));
    }
}