package de.jare.gildeddice.dtos.games.game;

import de.jare.gildeddice.dtos.games.choice.GameChoiceShortDTO;

import java.util.List;

public record GamePhaseDTO(

        String category,
        String title,
        String intro,
        boolean skippable,
        boolean gameEnd,
        List<GameChoiceShortDTO> choices
) {
}
