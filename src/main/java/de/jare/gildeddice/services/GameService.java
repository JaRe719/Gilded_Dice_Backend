package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.games.GamePhaseDTO;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.GameRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameRepository gameRepository;
    private UserService userService;





//    public GamePhaseDTO getGamePhase(Authentication auth) {
//        User user = userService.getUser(auth);
//        Game game = gameRepository.findByUser(user);
//
//        if (game == null) {
//            game = new Game();
//            game.setUser(user);
//            gameRepository.save(game);
//            game.setPhase(1);
//        }
//
//        return null;
//    }
}
