package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.games.game.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.game.GameChoiceResultDTO;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import de.jare.gildeddice.services.GameService;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            return ResponseEntity.ok(gameService.getGamePhase(auth));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping
    public ResponseEntity<Void> resetGame(Authentication auth) {
        try {
            gameService.resetGame(auth);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<GameChoiceResultDTO> playChoice(@PathVariable long choiceId, @RequestParam int diceResult, Authentication auth) {

        try {
            return ResponseEntity.ok(gameService.playChoice(choiceId, diceResult, auth));
        } catch (EntityNotFoundException en) {
           return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
