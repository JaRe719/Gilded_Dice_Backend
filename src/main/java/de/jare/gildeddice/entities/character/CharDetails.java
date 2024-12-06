package de.jare.gildeddice.entities.character;

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
    private int healthLvl = 10;

    private int intelligence = 0;
    private int negotiate = 0;
    private int ability = 0;
    private int planning = 0;
    private int stamina = 0;

    private int income = 0;
    private int outcome = 0;
    private int invest = 0;
    private int money = 300;

    private int simplification = 0;
    private int complication = 0;
    private String avatar;

    @OneToOne(cascade = {CascadeType.ALL})
    private CharChoices charChoices;

}
