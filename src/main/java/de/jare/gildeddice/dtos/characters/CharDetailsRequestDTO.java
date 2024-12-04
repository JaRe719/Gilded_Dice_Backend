package de.jare.gildeddice.dtos.characters;

public record CharDetailsRequestDTO(

        int intelligence,
        int negotiate,
        int ability,
        int planning,
        int stamina,
        String avatar
) {
}
