package de.jare.gildeddice.dtos.games;


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
