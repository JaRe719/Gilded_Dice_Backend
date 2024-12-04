package de.jare.gildeddice.controller;

import de.jare.gildeddice.services.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    private GameService gameService;



    @GetMapping(value = "/games")
    public String getAllGames() {
        return gameService.getAllGames();
    }

}
