package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.model.GameStatus;
import com.endgame.endgameapi.repository.UserGameRepository;
import com.endgame.endgameapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/game")
public class UserGameController {

    private final UserGameRepository userGameRepository;

    public UserGameController(UserGameRepository userGameRepository) {
        this.userGameRepository = userGameRepository;
    }

    @PatchMapping("/change/status/{id}")
    public ResponseEntity<UserGame> changeStatus(@PathVariable Long id, @RequestBody GameStatus newStatus) {
        return userGameRepository.findById(id).map(userGame -> {
            userGame.setStatus(newStatus);
            userGameRepository.save(userGame);
            return ResponseEntity.ok(userGame);
        }).orElse(ResponseEntity.notFound().build());
    }


}