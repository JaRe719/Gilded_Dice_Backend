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

    @OneToOne
    private CharDetails charDetails;
}
