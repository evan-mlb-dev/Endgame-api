package com.endgame.endgameapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


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

}