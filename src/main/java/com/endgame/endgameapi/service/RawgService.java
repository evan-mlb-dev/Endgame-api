package com.endgame.endgameapi.service;

import com.endgame.endgameapi.dto.GameDTO;
import com.endgame.endgameapi.dto.RawgResponseDTO;
import com.endgame.endgameapi.model.Game;
import com.endgame.endgameapi.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RawgService {

    private static final Logger logger = LoggerFactory.getLogger(RawgService.class);

    private final RestClient restClient;
    private final GameRepository gameRepository;
    private final String baseUrl = "https://api.rawg.io/api/games";
    @Value("${RAWG.API.KEY}")
    private String apiKey;

    public RawgService(GameRepository gameRepository) {
        this.restClient = RestClient.create();
        this.gameRepository = gameRepository;
    }

    public List<GameDTO> searchGames(String search) {
        String url = String.format("%s?key=%s&search=%s&page_size=10", baseUrl, apiKey, search);

        RawgResponseDTO response = restClient.get()
                .uri(url)
                .retrieve()
                .body(RawgResponseDTO.class);

        return (response != null && response.getResults() != null) ? response.getResults() : List.of();
    }

    public List<GameDTO> getGames(int page, int pageSize) {
        String url = String.format("%s?key=%s&page=%d&page_size=%d", baseUrl, apiKey, page, pageSize);

        RawgResponseDTO response = restClient.get()
                .uri(url)
                .retrieve()
                .body(RawgResponseDTO.class);

        return (response != null && response.getResults() != null) ? response.getResults() : List.of();
    }

    public int feedGames(int page, int pageSize) {
        List<GameDTO> gameDTOS = getGames(page, pageSize);
        logger.atInfo().log("{} games fetched from RAWG !", gameDTOS.size());

        List<Game> gamesToSave = gameDTOS.stream().map(dto ->
                gameRepository.findByRawgId(dto.getRawgId())
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
                        .orElseGet(() -> new Game(dto))
        ).toList();

        gameRepository.saveAll(gamesToSave);
        logger.atInfo().log("{} games processed (added/updated) !", gamesToSave.size());

        return gamesToSave.size();
    }

    public Object searchGamesFullData(String search) {
        String url = String.format("%s?key=%s&search=%s&page_size=10", baseUrl, apiKey, search);

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(Object.class);
    }

    public Object getGameDetails(Long gameId) {
        String url = String.format("%s/%d?key=%s", baseUrl, gameId, apiKey);

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(Object.class);
    }
}