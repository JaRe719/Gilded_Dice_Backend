package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.games.storys.Story;
import org.springframework.data.repository.CrudRepository;

public interface StoryRepository extends CrudRepository<Story, Long> {
    Story findByPhase(int phase);

}
