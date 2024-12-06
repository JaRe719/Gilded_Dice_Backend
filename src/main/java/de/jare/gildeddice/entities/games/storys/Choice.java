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

    @Column(length = 1024)
    private String startMessage;

    @Column(length = 1024)
    private String winMessage;
    private int winIncomeValue;
    private int winOutcomeValue;
    private int winInvestmentPercent;

    @Column(length = 1024)
    private String loseMessage;
    private int loseIncomeValue;
    private int loseOutcomeValue;
    private int loseInvestmentPercent;

    @Column(length = 1024)
    private String critMessage;
    private int critIncomeValue;
    private int critOutcomeValue;
    private int critInvestmentPercent;

    private int newStressValue;
    private int newSatisfactionValue;
    private int newHealthValue;


    @ManyToOne
    private Npc npc;
}
