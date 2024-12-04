package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.services.CharDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/char")
public class CharController {

    private CharDetailsService charDetailsService;

    public CharController(CharDetailsService charDetailsService) {
        this.charDetailsService = charDetailsService;
    }

    @GetMapping
    public ResponseEntity<CharDetailsResponseDTO> getCharDetails(Authentication auth) {
        try {
            return ResponseEntity.ok(charDetailsService.getCharDetails(auth));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CharDetailsResponseDTO> createOrUpdateCharDetails(@RequestBody CharDetailsRequestDTO dto, Authentication auth) {
        try {
            return ResponseEntity.ok(charDetailsService.createOrUpdateCharDetails(dto, auth));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
