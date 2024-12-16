package de.jare.gildeddice.dtos.games.choice;

public record GameChoiceShortDTO(
        long id,
        String title,
        Integer cost,
        boolean returning
) {
}
