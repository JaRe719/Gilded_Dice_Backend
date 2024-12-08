package de.jare.gildeddice.dtos.games.plusstorys;

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
