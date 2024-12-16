package de.jare.gildeddice.dtos.games.choice;

public record GameChoiceDTO(

        long id,
        String title,
        String skill,
        String startMessage,
        String npcName,
        String npcFilename
) {
}
