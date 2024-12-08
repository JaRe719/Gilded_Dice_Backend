package de.jare.gildeddice.mapper;

import de.jare.gildeddice.dtos.games.game.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.game.GameChoiceShortDTO;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.games.choices.Choice;

import java.util.List;

public class GameMapper {

    public static GamePhaseDTO toGamePhaseDTO(Category category, String message, boolean skippable, boolean gameEnd, List<Choice> choices) {
        return new GamePhaseDTO(category.name(), message, skippable, gameEnd, choices.stream().map(GameMapper::toGameChoiceShortDTO).toList());
    }

    private static GameChoiceShortDTO toGameChoiceShortDTO(Choice choice) {
        return new GameChoiceShortDTO(choice.getId(), choice.getTitle(), choice.getCost());
    }

    public static GameChoiceDTO toGameChoiceDTO(Choice choice) {
        return new GameChoiceDTO(
                choice.getId(),
                choice.getTitle(),
                choice.getSkill().name(),
                choice.getStartMessage(),
                choice.getNpc().getName(),
                choice.getNpc().getFilename()
        );
    }
}
