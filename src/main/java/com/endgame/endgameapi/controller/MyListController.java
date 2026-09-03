package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.service.UserGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-list")
public class MyListController {

    private final UserGameService userGameService;

    public MyListController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    @GetMapping
    public ResponseEntity<List<UserGame>> getMyList() {
        return ResponseEntity.ok(userGameService.getAllUserGames());
    }

    @PostMapping("/add")
    public ResponseEntity<UserGame> addToPlayList(@RequestBody UserGame userGame) {
        return ResponseEntity.ok(userGameService.saveUserGame(userGame));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeFromList(@PathVariable Long id) {
        boolean deleted = userGameService.removeUserGame(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}