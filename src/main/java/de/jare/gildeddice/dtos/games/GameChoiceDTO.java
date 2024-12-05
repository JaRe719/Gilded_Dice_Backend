package de.jare.gildeddice.dtos.games;

public record GameChoiceDTO(

        long id,
        String title,
        String skill,
        int minDiceValue,
        String startMessage,
        String npc
) {
}
