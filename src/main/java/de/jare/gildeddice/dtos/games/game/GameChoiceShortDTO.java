package de.jare.gildeddice.dtos.games.game;

public record GameChoiceShortDTO(
        long id,
        String title,
        Integer cost,
        boolean retuning
) {
}
