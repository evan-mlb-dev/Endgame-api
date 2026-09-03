package com.endgame.endgameapi.controller;

import com.endgame.endgameapi.service.RawgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class RawgController {

    private final RawgService rawgService;

    public RawgController(RawgService rawgService) {
        this.rawgService = rawgService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(rawgService.searchGames(name));
    }

    @GetMapping("/searchFullData")
    public ResponseEntity<?> searchFullData(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(rawgService.searchGamesFullData(name));
    }

    @GetMapping("/feed")
    public ResponseEntity<Integer> feed(@RequestParam int page, @RequestParam int pageSize) {
        int processedCount = rawgService.feedGames(page, pageSize);
        return ResponseEntity.ok(processedCount);
    }
}