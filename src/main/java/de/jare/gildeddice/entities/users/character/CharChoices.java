package de.jare.gildeddice.entities.users.character;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CharChoices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean study = false;
    private boolean scholarship = false;
    private boolean apprenticeship = false;
    private boolean job = false;

    private boolean property = false;
    private boolean rentApartment = false;
    private boolean car = false;
    private boolean driverLicense = false;
}
