package com.endgame.endgameapi.service;



import com.endgame.endgameapi.dto.GameDTO;
import com.endgame.endgameapi.dto.RawgResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class RawgService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://api.rawg.io/api/games";
    @Value("${rawg.api.key}")
    private String apiKey;


    public RawgService() {
        this.restTemplate = new RestTemplate();
    }

    public List<GameDTO> searchGames(String search) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(BASE_URL))
                .queryParam("key", apiKey)
                .queryParam("search", search)
                .queryParam("page_size", 10);

        RawgResponseDTO response = restTemplate.getForObject(builder.toUriString(), RawgResponseDTO.class);
        return (response != null) ? response.getResults() : List.of();
    }

    public List<GameDTO> searchGamesRandom(String search, int size) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(BASE_URL))
                .queryParam("key", apiKey)
                .queryParam("search", search)
                .queryParam("page_size", size);

        RawgResponseDTO response = restTemplate.getForObject(builder.toUriString(), RawgResponseDTO.class);

        if (response != null && response.getResults() != null) {
            List<GameDTO> games = response.getResults();
            Collections.shuffle(games);
            return games;
        }
        return List.of();
    }

    public Object searchGamesFullData(String search) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(BASE_URL))
                .queryParam("key", apiKey)
                .queryParam("search", search)
                .queryParam("page_size", 10);
        return restTemplate.getForObject(builder.toUriString(), Object.class);
    }



    public Object getGameDetails(Long gameId) {
        String url = BASE_URL + "/" + gameId + "?key=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }
}