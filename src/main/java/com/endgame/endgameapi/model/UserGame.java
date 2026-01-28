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
@Table(name = "userGames")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGame {
    private static final Logger logger = LoggerFactory.getLogger(RawgController.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(unique = true, nullable = false)
    private Long gameId;

    @Column(unique = true, nullable = false)
    private Long rawgId;

    @Enumerated(EnumType.STRING)
    @Column()
    private GameStatus status;

    @Column(length = 1000)
    private String personalNote;

    private Integer personalRating;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}