package de.jare.gildeddice.entities.users;

import de.jare.gildeddice.entities.users.character.CharDetails;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private int highScore;

    @OneToOne(cascade = {CascadeType.ALL})
    private CharDetails charDetails;
}
