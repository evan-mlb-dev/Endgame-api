package com.endgame.endgameapi.repository;

import com.endgame.endgameapi.model.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findById(Long id);

    Optional<UserGame> findByUserIdAndGameId(Long userId, Long gameId);

}