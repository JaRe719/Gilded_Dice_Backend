package de.jare.gildeddice.entities.games.storys;

import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.games.choices.Choice;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Category category;
    private String title;
    private int phase;
    private boolean skippable;
    private boolean phaseEnd;
    private String prompt;
    private boolean gameEnd;

    @OneToMany
    private List<Choice> choices = new ArrayList<>();
}
