package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.dto.UserGameResponseDto;
import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.model.User;
import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.service.CustomUserDetailsService;
import com.endgame.endgameapi.service.GameService;
import com.endgame.endgameapi.service.UserGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/usergame")
public class UserGameController {

    private final UserGameService userGameService;
    private final GameService gameService;
    private final CustomUserDetailsService userService;

    public UserGameController(UserGameService userGameService, CustomUserDetailsService userService, GameService gameService) {
        this.userGameService = userGameService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUserGames(@AuthenticationPrincipal User user, @RequestParam(required = false) GameStatus status) {
        Map<GameStatus, List<UserGame>> userGames;
        if (status != null) {
            userGames = userGameService.getUserGamesWithStatus(user.getId(), status);
        } else {
            userGames = userGameService.getUserGames(user.getId());
        }

        return ResponseEntity.ok(userGames);
    }


    @GetMapping("/counts")
    public ResponseEntity<?> getUserGamesCounts(@AuthenticationPrincipal User user) {
        Map<GameStatus, Long> userGamesCounts = userGameService.getGameStatusCountsForUser(user.getId());
        return ResponseEntity.ok(userGamesCounts);
    }


    @PostMapping
    public ResponseEntity<?> addUserGame(
            @AuthenticationPrincipal User user,
            @RequestParam Long gameId,
            @RequestParam GameStatus newStatus
    ) {

        log.info("ok");

        // 1. Check authentication & required params
        if (user == null || gameId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required or missing parameters.");
        }

        Long userId = user.getId();

        // 2. Validate User existence
        if (!userService.exist(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("User not found with ID: %d", userId));
        }

        // 3. Validate Game existence
        if (!gameService.exist(gameId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Game not found with ID: %s", gameId));
        }

        // 4. Add or update game in user list
        userGameService.addToUserList(userId, gameId, newStatus);

        // 5. Build response DTO
        UserGameResponseDto response = new UserGameResponseDto(
                String.format("Game status successfully updated to %s", newStatus),
                userId,
                gameId,
                newStatus
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
