package de.jare.gildeddice.dtos.games.plusstorys;

public record RequirenentDTO(

        Boolean hasStudie,
        Boolean hasScholarship,
        Boolean hasApprenticeship,
        Boolean hasSecondJob,
        Boolean hasJob,

        Boolean insurance,

        Boolean hasHomeByParents,
        Boolean hasSharedApartment,
        Boolean hasRentedApartment,

        Boolean hasProperty,
        Boolean hasCar,
        Boolean hasDriverLicense,

        Boolean hasInvested,
        Integer stressStatusLvl,
        Integer satisfactionStatusLvl,
        Integer healthStatusLvl
) {
}
