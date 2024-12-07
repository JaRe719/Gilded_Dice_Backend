package de.jare.gildeddice.entities.users.character;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CharDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int stressLvl = 0;
    private int satisfactionLvl = 5;
    private int healthLvl = 20;

    private int intelligence = 0;
    private int negotiate = 0;
    private int ability = 0;
    private int planning = 0;
    private int stamina = 0;

    private int income = 0;
    private int outcome = 0;
    private int invest = 0;
    private float investmentPercent = 0.0f;
    private int money = 300;

    private int handicap = 0;
    private String avatar;

    @OneToOne(cascade = {CascadeType.ALL})
    private CharChoices charChoices;

}
