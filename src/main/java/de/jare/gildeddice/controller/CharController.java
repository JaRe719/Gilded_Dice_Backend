package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.services.CharDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/char")
public class CharController {

    private CharDetailsService charDetailsService;

    public CharController(CharDetailsService charDetailsService) {
        this.charDetailsService = charDetailsService;
    }

    @GetMapping
    public CharDetailsResponseDTO getCharDetails(Authentication auth) {
        return charDetailsService.getCharDetails(auth);
    }
}
