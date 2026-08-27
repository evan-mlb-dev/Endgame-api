package com.endgame.endgameapi.model;

import com.endgame.endgameapi.controller.RawgController;
import com.endgame.endgameapi.dto.GameDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    private static final Logger logger = LoggerFactory.getLogger(RawgController.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long rawgId;

    @Column(nullable = false)
    private String name;

    private String backgroundImage;

    private Double rating;


    @ElementCollection
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "genre")
    @Builder.Default
    private List<String> genres = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_tags", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    private int playtime;

    private String released;

    private Double HowLongToBeat;

    private LocalDateTime createdAt;

    public Game(GameDTO dto) {
        this.rawgId = dto.getRawgId();
        this.name = dto.getName();
        this.backgroundImage = dto.getBackgroundImage();
        this.rating = dto.getRating();
        this.playtime = dto.getPlaytime();
        this.released = dto.getReleased();
        this.genres = dto.getGenres();
        this.tags = dto.getTags();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}