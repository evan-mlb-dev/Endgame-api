package com.endgame.endgameapi.service;


import com.endgame.endgameapi.dto.GameDTO;
import com.endgame.endgameapi.dto.RawgResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class RawgService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://api.rawg.io/api/games";
    @Value("${RAWG.API.KEY}")
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

    public List<GameDTO> getGames(int page, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(BASE_URL))
                .queryParam("key", apiKey)
                .queryParam("page",page)
                .queryParam("page_size", pageSize);

        RawgResponseDTO response = restTemplate.getForObject(builder.toUriString(), RawgResponseDTO.class);

        if (response != null && response.getResults() != null) {
            return response.getResults();
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