package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.HighScore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HighScoreRepository extends CrudRepository<HighScore, String> {
    @Override
    List<HighScore> findAll();

    Optional<HighScore> findByUsername(String username);
}
