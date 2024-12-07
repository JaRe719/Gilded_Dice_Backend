package de.jare.gildeddice.services;

import de.jare.gildeddice.entities.HighScore;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.repositories.HighScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HighScoreServiceTest {

    @Mock
    private HighScoreRepository highScoreRepository;

    private HighScoreService highScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        highScoreService = new HighScoreService(highScoreRepository);
    }

    @Test
    void testGetAllHighscores_Success() {
        // Arrange
        HighScore score1 = new HighScore("Player1", 100);
        HighScore score2 = new HighScore("Player2", 200);
        HighScore score3 = new HighScore("Player3", 150);

        when(highScoreRepository.findAll()).thenReturn(Arrays.asList(score1, score2, score3));

        // Act
        List<HighScore> result = highScoreService.getAllHighscores();

        // Assert
        assertEquals(3, result.size());
        assertEquals("Player2", result.get(0).getUsername()); // Highest score first
        assertEquals("Player3", result.get(1).getUsername());
        assertEquals("Player1", result.get(2).getUsername());
        verify(highScoreRepository, times(1)).findAll();
    }

    @Test
    void testGetAllHighscores_EmptyList() {
        // Arrange
        when(highScoreRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<HighScore> result = highScoreService.getAllHighscores();

        // Assert
        assertEquals(0, result.size());
        verify(highScoreRepository, times(1)).findAll();
    }

    @Test
    void testGetTopTen_Success() {
        // Arrange
        HighScore score1 = new HighScore("Player1", 100);
        HighScore score2 = new HighScore("Player2", 200);
        HighScore score3 = new HighScore("Player3", 150);
        HighScore score4 = new HighScore("Player4", 180);
        HighScore score5 = new HighScore("Player5", 170);
        HighScore score6 = new HighScore("Player6", 140);
        HighScore score7 = new HighScore("Player7", 130);
        HighScore score8 = new HighScore("Player8", 120);
        HighScore score9 = new HighScore("Player9", 110);
        HighScore score10 = new HighScore("Player10", 90);
        HighScore score11 = new HighScore("Player11", -80);

        when(highScoreRepository.findAll()).thenReturn(
                Arrays.asList(score2, score4, score5, score3, score6, score7, score8, score9, score1, score10, score11)
        );

        // Act
        List<HighScore> result = highScoreService.getTopTen();

        // Assert
        assertEquals(10, result.size());
        assertEquals("Player2", result.get(0).getUsername()); // Highest score first
        assertEquals("Player10", result.get(9).getUsername());  // 10th highest score
        verify(highScoreRepository, times(1)).findAll();
    }

    @Test
    void testGetTopTen_LessThanTenScores() {
        // Arrange
        HighScore score1 = new HighScore("Player1", 100);
        HighScore score2 = new HighScore("Player2", 200);

        when(highScoreRepository.findAll()).thenReturn(Arrays.asList(score1, score2));

        // Act
        List<HighScore> result = highScoreService.getTopTen();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Player2", result.get(0).getUsername()); // Highest score first
        assertEquals("Player1", result.get(1).getUsername());
        verify(highScoreRepository, times(1)).findAll();
    }

    @Test
    void testSaveHighscore_SaveNewHighScore() {
        // Arrange
        int highScore = 12345;
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("tester");
        profile.setHighScore(highScore);

        HighScore score = new HighScore("tester", highScore);
        when(highScoreRepository.findByUsername(profile.getUsername())).thenReturn(Optional.empty());
        when(highScoreRepository.save(score)).thenReturn(score);

        // Act
        highScoreService.saveHighScore(profile);

        // Assert
        verify(highScoreRepository, times(1)).save(score);
    }

    @Test
    void testSaveHighscore_UpdateExistingHighScore() {
        // Arrange
        int highScore = 12345;
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("tester");
        profile.setHighScore(highScore);

        HighScore existingScore = new HighScore("tester", 100);
        when(highScoreRepository.findByUsername(profile.getUsername())).thenReturn(Optional.of(existingScore));
        when(highScoreRepository.save(existingScore)).thenReturn(existingScore);

        // Act
        highScoreService.saveHighScore(profile);

        // Assert
        verify(highScoreRepository, times(1)).save(existingScore);
    }

    @Test
    void testSaveHighscore_newHighScoreIsLowerAsinHighscoreEntity() {
        // Arrange
        int highScore = 12345;
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("tester");
        profile.setHighScore(highScore);

        HighScore existingScore = new HighScore("tester", 15000);
        when(highScoreRepository.findByUsername(profile.getUsername())).thenReturn(Optional.of(existingScore));

        //Act & Assert

        assertThrows(IllegalStateException.class, () -> highScoreService.saveHighScore(profile));
        verify(highScoreRepository, never()).save(any(HighScore.class));
    }

}
