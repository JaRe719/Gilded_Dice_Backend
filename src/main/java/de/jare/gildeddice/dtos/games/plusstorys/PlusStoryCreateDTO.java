package de.jare.gildeddice.dtos.games.plusstorys;

import de.jare.gildeddice.dtos.games.choice.ChoiceCreateDTO;

import java.util.List;

public record PlusStoryCreateDTO(
        String category,
        String title,
        String prompt,
        boolean skippable,
        boolean oneTime,
        RequirenentDTO requirement,
        List<ChoiceCreateDTO> choices
) {
}
