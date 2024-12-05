package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.games.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.GameChoiceResultDTO;
import de.jare.gildeddice.dtos.games.GamePhaseDTO;
import de.jare.gildeddice.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/choice/{choiceId}")
    public ResponseEntity<GameChoiceDTO> getChoiceDetails(@PathVariable long choiceId) {
        try {
            return ResponseEntity.ok(gameService.getChoiceDetails(choiceId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/choice/{choiceId}")
    public ResponseEntity<GameChoiceResultDTO> calculateChoiceDiceValue(@PathVariable long choiceId, @RequestParam int diceResult) {
        try {
            return ResponseEntity.ok(gameService.calculateChoiceDiceValue(choiceId, diceResult));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
