package de.jare.gildeddice.mapper;

import de.jare.gildeddice.dtos.games.GameChoiceShortDTO;
import de.jare.gildeddice.dtos.games.GamePhaseDTO;
import de.jare.gildeddice.entities.games.storys.Choice;

import java.util.List;

public class GameMapper {

    public static GamePhaseDTO toGamePhaseDTO(String message, List<Choice> choices) {
        return new GamePhaseDTO(message, choices.stream().map(GameMapper::toGameChoiceShortDTO).toList());
    }

    private static GameChoiceShortDTO toGameChoiceShortDTO(Choice choice) {
        return new GameChoiceShortDTO(choice.getId(), choice.getTitle());
    }
}