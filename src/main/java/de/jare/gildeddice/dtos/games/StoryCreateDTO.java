package de.jare.gildeddice.dtos.games;


import java.util.List;

public record StoryCreateDTO(
        String category,
        String title,
        int phase,
        String prompt,
        List<ChoiceCreateDTO> choices
        ) {

}
