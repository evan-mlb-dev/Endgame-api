package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.model.UserGame;
import com.endgame.endgameapi.repository.GameRepository;
import com.endgame.endgameapi.repository.UserGameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/my-list")
public class MyListController {
    private final UserGameRepository userGameRepository;

    public MyListController(UserGameRepository userGameRepository) {
        this.userGameRepository = userGameRepository;
    }

    @GetMapping
    public List<UserGame> getMyList() {
        return userGameRepository.findAll();
    }

    @PostMapping("/add")
    public UserGame addToPlayList(@RequestBody UserGame userGame) {
        return userGameRepository.save(userGame);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeFromList(@PathVariable Long id) {
        if (userGameRepository.existsById(id)) {
            userGameRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}