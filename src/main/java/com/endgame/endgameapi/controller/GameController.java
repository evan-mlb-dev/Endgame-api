package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.service.RawgService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final RawgService rawgService;

    public GameController(RawgService rawgService) {
        this.rawgService = rawgService;
    }

    @GetMapping("/search")
    public Object search(@RequestParam(required = false) String name) {
        return rawgService.searchGames(name);
    }

    @GetMapping("/searchFullData")
    public Object searchFullData(@RequestParam(required = false) String name) {
        return rawgService.searchGamesFullData(name);
    }

    @GetMapping("/searchRandom")
    public Object searchRandom(@RequestParam(required = false) String name) {
        return rawgService.searchGamesRandom(name,20);
    }
}