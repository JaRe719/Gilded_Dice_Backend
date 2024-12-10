package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.games.choice.ChoiceUpdateDTO;
import de.jare.gildeddice.dtos.games.game.NpcCreateListDTO;
import de.jare.gildeddice.dtos.games.plusstorys.PlusStoryCreateDTO;
import de.jare.gildeddice.dtos.games.story.StoryCreateDTO;
import de.jare.gildeddice.dtos.games.story.StoryUpdateDTO;
import de.jare.gildeddice.entities.games.storys.Npc;
import de.jare.gildeddice.entities.games.choices.Choice;
import de.jare.gildeddice.dtos.games.plusstorys.PlusStoryUpdateDTO;
import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    private GameService gameService;

    public AdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = "/storys")
    public ResponseEntity<Iterable<Story>> getAllStorys(Authentication auth) {
        System.out.println(auth.getAuthorities());
        return ResponseEntity.ok(gameService.getAllStorys());
    }

    @PostMapping(value = "/storys")
    public ResponseEntity<Void> CreateStory(@RequestBody StoryCreateDTO dto) {
        gameService.createStory(dto);
        return ResponseEntity.ok().build();

    }

    @PutMapping(value = "/storys")
    public ResponseEntity<Void> updateStory(@RequestBody StoryUpdateDTO dto) {
        try {
            gameService.updateStory(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //------

    @PostMapping(value = "/plusstorys")
    public ResponseEntity<Void> createPlusStory(@RequestBody PlusStoryCreateDTO dto) {
            gameService.createPlusStory(dto);
            return ResponseEntity.ok().build();

    }

    @PutMapping(value = "/plusstorys")
    public ResponseEntity<Void> updatePlusStory(@RequestBody PlusStoryUpdateDTO dto) {
        gameService.updatePlusStory(dto);
        return ResponseEntity.ok().build();
    }

    //------

    @GetMapping(value = "/npc")
    public ResponseEntity<Iterable<Npc>> getAllNpc() {
        return ResponseEntity.ok(gameService.getAllNpc());
    }

    @PostMapping(value = "/npc")
    public ResponseEntity<Void> createNpc(@RequestParam String npcName, String filename) {
        gameService.createNpc(npcName, filename);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/npclist")
    public ResponseEntity<Void> createNpcFromList(@RequestBody List<NpcCreateListDTO> dto) {
        gameService.createNpcFromList(dto);
        return ResponseEntity.ok().build();
    }

    //------

    @GetMapping(value = "/choice")
    public ResponseEntity<Iterable<Choice>> getAllChoice() {
        return ResponseEntity.ok(gameService.getAllChoice());
    }

    @PutMapping(value = "/choice")
    public ResponseEntity<Void> updateChoice(@RequestBody ChoiceUpdateDTO dto) {
        gameService.updateChoice(dto);
        return ResponseEntity.ok().build();
    }
}
