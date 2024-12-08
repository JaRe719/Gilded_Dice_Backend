package de.jare.gildeddice.dtos.characters;

public record CharDetailsResponseDTO(

        long id,
        int stressLvl,
        int satisfactionLvl,
        int healthLvl,

        int intelligence,
        int negotiate,
        int ability,
        int planning,
        int stamina,
        int handycap,

        boolean study,
        boolean scholarship,
        boolean apprenticeship,
        boolean job,

        boolean property,
        boolean rentApartment,
        boolean car,
        boolean driverLicense,

        String avatar
) {
}
