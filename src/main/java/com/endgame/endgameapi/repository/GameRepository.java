package com.endgame.endgameapi.repository;

import com.endgame.endgameapi.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByRawgId(Long rawgId);

    boolean existsByRawgId(Long rawgId);

    boolean existsById(Long id);

    @Query(value = "SELECT * FROM games WHERE rating > 3.1 ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Game> find50RandomGames();

    List<Game> findByNameContainingIgnoreCase(String name);
}