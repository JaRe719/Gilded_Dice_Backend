package de.jare.gildeddice.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = false)
public class GamePhaseDTOConverter implements AttributeConverter<GamePhaseDTO, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(GamePhaseDTO attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize GamePhaseDTO to JSON", e);
        }
    }

    @Override
    public GamePhaseDTO convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return objectMapper.readValue(dbData, GamePhaseDTO.class);
        } catch (IOException e) {
            throw new IllegalStateException("Could not deserialize JSON to GamePhaseDTO", e);
        }
    }
}

