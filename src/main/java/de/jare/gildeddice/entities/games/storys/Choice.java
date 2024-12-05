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

    @Column(length = 1024)
    private int minDiceValue;
    @Column(length = 1024)
    private String startMessage;
    @Column(length = 1024)
    private String winMessage;
    @Column(length = 1024)
    private String loseMessage;
    @Column(length = 1024)
    private String critMessage;

    @ManyToOne
    private Npc npc;
}
