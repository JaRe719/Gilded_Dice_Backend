package de.jare.gildeddice.controller;

import de.jare.gildeddice.entities.HighScore;
import de.jare.gildeddice.services.HighScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/api/v1/highscore")
public class HighScoreController {

    private HighScoreService highScoreService;


    public HighScoreController(HighScoreService highScoreService) {
        this.highScoreService = highScoreService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<HighScore>> getAllHighscores() {
        return ResponseEntity.ok(highScoreService.getAllHighscores());
    }

    @GetMapping(value = "/toplist")
    public ResponseEntity<List<HighScore>> getTopHighscores() {
        return ResponseEntity.ok(highScoreService.getTopTen());
    }
}
