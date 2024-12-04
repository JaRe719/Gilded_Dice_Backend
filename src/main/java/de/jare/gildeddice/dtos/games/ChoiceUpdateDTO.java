package de.jare.gildeddice.dtos.games;

public record ChoiceUpdateDTO(
        long id,
        String title,
        String skill,
        int minDiceValue,
        String startMessage,
        String winMessage,
        String loseMessage,
        String critMessage,
        long npcId
) {
}
