package com.endgame.endgameapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_games",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "game_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long gameId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
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