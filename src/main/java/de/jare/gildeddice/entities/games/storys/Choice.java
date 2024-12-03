package de.jare.gildeddice.entities.games.storys;

import de.jare.gildeddice.entities.Npc;
import de.jare.gildeddice.entities.enums.Skill;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private Skill skill;
    private int minDiceValue;
    private String startMessage;
    private String winMessage;
    private String loseMessage;
    private String critMessage;

    @OneToOne
    private Npc npc;
}
