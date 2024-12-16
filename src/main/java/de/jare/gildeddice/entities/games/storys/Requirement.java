package de.jare.gildeddice.entities.games.storys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Boolean hasStudie;
    Boolean hasScholarship;
    Boolean hasApprenticeship;
    Boolean hasSecondJob;
    Boolean hasJob;

    Boolean insurance;

    Boolean hasHomeByParents;
    Boolean hasSharedApartment;
    Boolean hasRentedApartment;

    Boolean hasProperty;
    Boolean hasCar;
    Boolean hasDriverLicense;

    Boolean hasInvested;
    Integer stressStatusLvl;
    Integer satisfactionStatusLvl;
    Integer healthStatusLvl;
}
