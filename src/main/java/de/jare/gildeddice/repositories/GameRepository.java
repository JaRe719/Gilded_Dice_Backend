package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.users.User;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
    Game findByUser(User user);

}
