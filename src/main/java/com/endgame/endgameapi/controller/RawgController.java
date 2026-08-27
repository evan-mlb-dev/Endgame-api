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
    public int feed(@RequestParam int page, @RequestParam int pageSize) {
        List<GameDTO> gameDTOS = rawgService.getGames(page, pageSize);
        logger.atInfo().log("{} games fetched from RAWG !", gameDTOS.size());

        List<Game> gamesToSave = gameDTOS.stream().map(dto -> {
            return gameRepository.findByRawgId(dto.getRawgId())
                    .map(existingGame -> {
                        existingGame.setName(dto.getName());
                        existingGame.setBackgroundImage(dto.getBackgroundImage());
                        existingGame.setRating(dto.getRating());
                        existingGame.setPlaytime(dto.getPlaytime());
                        existingGame.setReleased(dto.getReleased());
                        existingGame.setGenres(dto.getGenres());
                        existingGame.setTags(dto.getTags());
                        return existingGame;
                    })
                    .orElseGet(() -> new Game(dto));
        }).toList();

        gameRepository.saveAll(gamesToSave);
        logger.atInfo().log("{} games processed (added/updated) !", gamesToSave.size());

        return gamesToSave.size();
    }
}