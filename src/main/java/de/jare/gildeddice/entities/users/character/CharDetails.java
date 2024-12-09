package de.jare.gildeddice.entities.users.character;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CharDetails {

    private static final int STRESS_MIN = 0;
    private static final int STRESS_MAX = 10;
    private static final int SATISFACTION_MIN = 0;
    private static final int SATISFACTION_MAX = 10;
    private static final int HEALTH_MIN = 0;
    private static final int HEALTH_MAX = 20;

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

    public void setStressLvl(int value) {
        this.stressLvl = clamp(this.stressLvl + value, STRESS_MIN, STRESS_MAX);
    }
    
    public void setSatisfactionLvl(int value) {
        this.satisfactionLvl = clamp(this.satisfactionLvl + value, SATISFACTION_MIN, SATISFACTION_MAX);
    }

    public void setHealthLvl(int value) {
        this.healthLvl = clamp(this.healthLvl + value, HEALTH_MIN, HEALTH_MAX);
    }

    private int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(value, max));
    }

}
