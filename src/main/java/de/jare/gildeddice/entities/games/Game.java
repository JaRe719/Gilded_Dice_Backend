package de.jare.gildeddice.entities.games;

import de.jare.gildeddice.converters.GamePhaseDTOConverter;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import de.jare.gildeddice.entities.games.storys.PlusStory;
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

    @Convert(converter = GamePhaseDTOConverter.class)
    @Column(columnDefinition = "text")
    private GamePhaseDTO currentGamePhase;

    @ElementCollection
    @CollectionTable(
            name = "game_available_plus_stories",
            joinColumns = @JoinColumn(name = "game_id")
    )
    @Column(name = "plus_story_id")
    private Set<Long> availablePlusStories = new HashSet<>();

    @ElementCollection
    private Set<Long> usedPlusStories = new HashSet<>();

}
