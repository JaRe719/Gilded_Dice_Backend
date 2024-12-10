package de.jare.gildeddice.entities.games;

import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.entities.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int phase;
    private int playedPhase = 0;
    private String username;
    private boolean gameLost = false;
    private boolean gameEnd = false;

    private boolean plusStoryRunLastRound = false;

    @OneToMany
    private List<PlusStory> availablePlusStories = new ArrayList<>();

    @ElementCollection
    private Set<Long> usedPlusStories = new HashSet<>();



}
