package de.jare.gildeddice.dtos.games.game;

public record GameChoiceResultDTO(
        boolean choiceWon,
        boolean gameLost,
        String endMessage,
        String calc
) {
}
