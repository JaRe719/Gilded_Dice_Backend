package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.games.storys.PlusStory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlusStoryRepository extends CrudRepository<PlusStory, Long> {
    @Override
    List<PlusStory> findAll();
}
