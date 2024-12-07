package de.jare.gildeddice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class HighScore {

    @Id
    private String username;

    private int score;

    public HighScore() {
    }

    public HighScore(String username, int score) {
        this.username = username;
        this.score = score;
    }
}
