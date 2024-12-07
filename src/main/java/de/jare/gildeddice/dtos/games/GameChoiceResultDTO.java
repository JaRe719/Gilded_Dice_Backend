package de.jare.gildeddice.dtos.games;

public record GameChoiceResultDTO(
        boolean choiceWon,
        boolean gameLost,
        String endMessage,
        String calc
) {
}
