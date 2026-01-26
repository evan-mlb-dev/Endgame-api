package com.endgame.endgameapi.model;

import com.endgame.endgameapi.controller.RawgController;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column()
    private GameStatus status = null;

    @Column(length = 1000)
    private String personalNote;

    private Integer personalRating;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}