package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.dto.GameDTO;
import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import com.endgame.endgameapi.service.RawgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class RawgController {
    private static final Logger logger = LoggerFactory.getLogger(RawgController.class);

    private final RawgService rawgService;
    private final GameRepository gameRepository;

    public RawgController(RawgService rawgService, GameRepository gameRepository) {
        this.rawgService = rawgService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/search")
    public Object search(@RequestParam(required = false) String name) {
        return rawgService.searchGames(name);
    }

    @GetMapping("/searchFullData")
    public Object searchFullData(@RequestParam(required = false) String name) {
        return rawgService.searchGamesFullData(name);
    }


    @GetMapping("/feed")
    public List<Game> feed(@RequestParam int page, @RequestParam int pageSize) {
        List<GameDTO> gameDTOS = rawgService.getGames(page,pageSize);
        logger.atInfo().log("{} games found !", gameDTOS.size());
        List<Game> games = gameDTOS.stream()
                .filter(dto -> !gameRepository.existsByRawgId(dto.getRawgId()))
                .map(Game::new)
                .toList();
        logger.atInfo().log("{} new games added !", games.size());
        return  gameRepository.saveAll(games);
    }
}