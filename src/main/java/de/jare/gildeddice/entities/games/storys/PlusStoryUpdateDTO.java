package de.jare.gildeddice.entities.games.storys;

import de.jare.gildeddice.dtos.games.plusstorys.RequirenentDTO;

import java.util.List;

public record PlusStoryUpdateDTO(
        long id,
        String category,
        String title,
        String prompt,
        boolean skippable,
        boolean oneTime,
        RequirenentDTO requirement
) {
}
