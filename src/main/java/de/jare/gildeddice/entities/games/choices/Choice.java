package de.jare.gildeddice.entities.games.choices;

import de.jare.gildeddice.entities.games.storys.Npc;
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
    private Integer cost;

    @Column(length = 1024)
    private String startMessage;

    @Column(length = 1024)
    private String winMessage;
    private Integer winIncomeValue;
    private Integer winOutcomeValue;
    private Float winInvestmentPercent;
    private Integer winOneTimePayment;
    private Boolean winStudy;
    private Boolean winScholarship;
    private Boolean winApprenticeship;
    private Boolean winJob;
    private Boolean winProperty;
    private Boolean winRentApartment;
    private Boolean winCar;

    private Integer winStressValue;
    private Integer winSatisfactionValue;
    private Integer winHealthValue;

    @Column(length = 1024)
    private String loseMessage;
    private Integer loseIncomeValue;
    private Integer loseOutcomeValue;
    private Float loseInvestmentPercent;
    private Integer loseOneTimePayment;
    private Boolean loseStudy;
    private Boolean loseScholarship;
    private Boolean loseApprenticeship;
    private Boolean loseJob;
    private Boolean loseProperty;
    private Boolean loseRentApartment;
    private Boolean loseCar;

    private Integer loseStressValue;
    private Integer loseSatisfactionValue;
    private Integer loseHealthValue;

    @Column(length = 1024)
    private String critMessage;
    private Integer critIncomeValue;
    private Integer critOutcomeValue;
    private Float critInvestmentPercent;
    private Integer critOneTimePayment;

    private Boolean critScholarship;

    private Integer critStressValue;
    private Integer critSatisfactionValue;
    private Integer critHealthValue;


    @ManyToOne
    private Npc npc;
}
