package de.jare.gildeddice.entities.games.storys;

import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.games.choices.Choice;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class PlusStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Category category;
    private String title;
    private String prompt;
    private boolean skippable;
    private boolean oneTime;

    @OneToOne(cascade = {CascadeType.ALL})
    private Requirement requirement;
    @OneToMany
    private List<Choice> choices = new ArrayList<>();
}

