package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.games.choices.Choice;
import org.springframework.data.repository.CrudRepository;

public interface ChoiceRepository extends CrudRepository<Choice, Long> {

}
