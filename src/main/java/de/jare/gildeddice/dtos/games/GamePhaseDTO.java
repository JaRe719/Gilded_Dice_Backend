package de.jare.gildeddice.dtos.games;

import java.util.List;

public record GamePhaseDTO(
        String intro,
        List<GameChoiceShortDTO> choices
) {
}