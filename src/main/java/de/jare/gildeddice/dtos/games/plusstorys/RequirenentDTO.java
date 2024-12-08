package de.jare.gildeddice.dtos.games.plusstorys;

public record RequirenentDTO(
        Boolean hasStudie,
        Boolean hasApprenticeship,
        Boolean hasJob,
        Boolean hasProperty,
        Boolean hasRentedApartment,
        Boolean hasCar,
        Boolean hasInvested,
        Integer stressStatusLvl,
        Integer satisfactionStatusLvl,
        Integer healthStatusLvl
) {
}
