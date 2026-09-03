package com.endgame.endgameapi.service;

import com.endgame.endgameapi.dto.UserGameDTO;
import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.repository.UserGameRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserGameService {

    private final UserGameRepository userGameRepository;

    public UserGameService(UserGameRepository userGameRepository) {
        this.userGameRepository = userGameRepository;
    }


    public boolean removeUserGame(Long id) {
        if (userGameRepository.existsById(id)) {
            userGameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void addToUserList(Long userId, Long gameId, GameStatus gameStatus) {
        Optional<UserGame> optionalUserGame = userGameRepository.findByUserIdAndGameId(userId, gameId);

        if (optionalUserGame.isPresent()) {
            this.updateUserGameStatus(optionalUserGame.get(), gameStatus);
        } else {
            UserGameDTO dto = new UserGameDTO(userId, gameId, gameStatus);
            createUserGame(userId, dto);
        }
    }

    private void updateUserGameStatus(UserGame userGame, GameStatus gameStatus) {
        userGame.setStatus(gameStatus);
    }

    public void createUserGame(Long userId, UserGameDTO dto) {
        UserGame newGame = UserGame.builder()
                .userId(userId)
                .gameId(dto.gameId())
                .status(dto.status())
                .build();
        userGameRepository.save(newGame);
    }

    public Map<GameStatus, List<UserGame>> getUserGames(Long userId) {
        List<UserGame> userGames = userGameRepository.findByUserId(userId);
        return userGames.stream()
                .collect(Collectors.groupingBy(UserGame::getStatus));
    }

    public Map<GameStatus, List<UserGame>> getUserGamesWithStatus(Long userId, GameStatus status) {
        List<UserGame> userGames = userGameRepository.findByUserIdAndStatus(userId, status);
        return userGames.stream()
                .collect(Collectors.groupingBy(UserGame::getStatus));
    }

    public Map<GameStatus, Long> getGameStatusCountsForUser(Long userId) {
        List<Object[]> results = userGameRepository.countUserGamesByStatus(userId);

        return results.stream()
                .collect(Collectors.toMap(
                        result -> (GameStatus) result[0],
                        result -> (Long) result[1]
                ));
    }
}