package de.jare.gildeddice.services;

import de.jare.gildeddice.entities.HighScore;
import de.jare.gildeddice.repositories.HighScoreRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HighScoreService {

    private HighScoreRepository highScoreRepository;

    public HighScoreService(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }

    public List<HighScore> getAllHighscores() {
        return highScoreRepository.findAll().stream()
                .sorted(Comparator.comparingInt(HighScore::getScore).reversed())
                .collect(Collectors.toList());
    }

    public List<HighScore> getTopTen() {
        return getAllHighscores().stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
