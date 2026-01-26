package com.endgame.endgameapi.dto;

import java.util.List;

public class RawgResponseDTO {
    private List<GameDTO> results;

    public List<GameDTO> getResults() { return results; }
    public void setResults(List<GameDTO> results) { this.results = results; }
}