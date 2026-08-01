package com.endgame.endgameapi.repository;

import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.model.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findById(Long id);

    List<UserGame> findByUserId(Long userId);

    Optional<UserGame> findByUserIdAndGameId(Long userId, Long gameId);

    List<UserGame> findByUserIdAndStatus(Long userId, GameStatus status);


    @Query("SELECT ug.status, COUNT(ug) FROM UserGame ug WHERE ug.userId = :userId GROUP BY ug.status")
    List<Object[]> countUserGamesByStatus(@Param("userId") Long userId);

}