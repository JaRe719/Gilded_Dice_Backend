package de.jare.gildeddice.dtos.games.story;


import de.jare.gildeddice.dtos.games.choice.ChoiceCreateDTO;

import java.util.List;

public record StoryCreateDTO(
        String category,
        String title,
        int phase,
        boolean phaseEnd,
        String prompt,
        boolean gameEnd,
        List<ChoiceCreateDTO> choices
        ) {

}
