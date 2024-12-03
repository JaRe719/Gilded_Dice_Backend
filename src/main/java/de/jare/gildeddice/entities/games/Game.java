package de.jare.gildeddice.entities.games;

import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.entities.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int phase;

    @OneToOne
    private User user;

    @OneToMany
    private List<Story> stories = new ArrayList<>();

}
