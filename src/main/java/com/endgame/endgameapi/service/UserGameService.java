package com.endgame.endgameapi.service;

import com.endgame.endgameapi.dto.UserGameDTO;
import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.repository.GameRepository;
import com.endgame.endgameapi.repository.UserGameRepository;
import com.endgame.endgameapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserGameService {

    public final UserGameRepository userGameRepository;


    public UserGameService(UserGameRepository userGameRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.userGameRepository = userGameRepository;

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
}
