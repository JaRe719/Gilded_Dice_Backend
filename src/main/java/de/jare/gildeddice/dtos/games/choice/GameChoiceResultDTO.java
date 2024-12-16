package de.jare.gildeddice.dtos.games.choice;

public record GameChoiceResultDTO(
        boolean choiceWon,
        boolean gameLost,
        String endMessage,
        String calc
) {
}
