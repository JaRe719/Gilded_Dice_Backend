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

    Boolean hasStudied;
    Boolean hasApprenticeship;
    Boolean hasJob;
    Boolean hasProperty;
    Boolean hasRentedApartment;
    Boolean hasCar;

    Boolean hasInvested;

    Integer stressStatusLvl;
    Integer satisfactionStatusLvl;
    Integer healthStatusLvl;


}
