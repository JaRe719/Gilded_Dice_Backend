package de.jare.gildeddice.dtos.games.game;

import de.jare.gildeddice.dtos.games.game.GameChoiceShortDTO;

import java.util.List;

public record GamePhaseDTO(

        String category,
        String intro,
        boolean skippable,
        List<GameChoiceShortDTO> choices
) {
}
