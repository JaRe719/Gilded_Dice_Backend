package de.jare.gildeddice.dtos.games.story;

public record StoryUpdateDTO(
        long id,
        String category,
        String title,
        int phase,
        boolean phaseEnd,
        String prompt,
        boolean skippable,
        boolean gameEnd
) {
}
