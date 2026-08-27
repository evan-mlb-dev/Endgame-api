package com.endgame.endgameapi.service;

import com.endgame.endgameapi.dto.GameDTO;
import com.endgame.endgameapi.dto.RawgResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RawgService {

    private final RestClient restClient;
    private final String baseUrl = "https://api.rawg.io/api/games";

    @Value("${RAWG.API.KEY}")
    private String apiKey;

    public RawgService() {
        this.restClient = RestClient.create();
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