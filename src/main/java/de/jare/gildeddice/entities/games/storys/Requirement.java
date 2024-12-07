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

    boolean hasStudied;
    boolean hasApprenticeship;
    boolean hasJob;
    boolean hasProperty;
    boolean hasRentedApartment;
    boolean hasCar;

    boolean hasInvested;

    Integer HealthStatusLvl;
    Integer StressStatusLvl;
    Integer SatisfactionStatusLvl;

}
