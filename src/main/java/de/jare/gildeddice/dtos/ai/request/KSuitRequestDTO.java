package de.jare.gildeddice.dtos.ai.request;

public record KSuitRequestDTO(
        KSuitMessageRequestDTO messages,
        String model
) {
}
