package com.endgame.endgameapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {

    @JsonProperty("id")
    private Long rawgId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("released")
    private String released;

    @JsonProperty("background_image")
    private String backgroundImage;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("playtime")
    private int playtime;

    @JsonProperty("genres")
    private List<GenreDTO> rawgGenres;

    @JsonProperty("tags")
    private List<TagDTO> rawgTags;


    @JsonIgnore
    public List<String> getGenres() {
        if (rawgGenres == null) return List.of();
        return rawgGenres.stream()
                .map(GenreDTO::getName)
                .collect(Collectors.toList());
    }


    @JsonIgnore
    public List<String> getTags() {
        if (rawgTags == null) return List.of();
        return rawgTags.stream()
                .map(TagDTO::getName)
                .collect(Collectors.toList());
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GenreDTO {
        private Long id;
        private String name;
        private String slug;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TagDTO {
        private Long id;
        private String name;
        private String slug;
    }
}