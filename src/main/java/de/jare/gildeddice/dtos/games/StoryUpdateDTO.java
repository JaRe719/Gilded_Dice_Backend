package de.jare.gildeddice.dtos.games;

public record StoryUpdateDTO(
        long id,
        String category,
        String title,
        int phase,
        String prompt
) {
}
