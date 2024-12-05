package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.games.GamePhaseDTO;
import de.jare.gildeddice.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/game")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<GamePhaseDTO> getGamePhase(Authentication auth) {
        return ResponseEntity.ok(gameService.getGamePhase(auth));
    }
}
