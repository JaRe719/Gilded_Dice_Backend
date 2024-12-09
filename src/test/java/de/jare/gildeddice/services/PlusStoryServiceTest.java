package de.jare.gildeddice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.repositories.PlusStoryRepository;
import de.jare.gildeddice.services.PlusStoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PlusStoryServiceTest {

    @Mock
    private PlusStoryRepository plusStoryRepository;

    @InjectMocks
    private PlusStoryService plusStoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPlusStory_Success() {
        // Arrange
        PlusStory story1 = new PlusStory();
        PlusStory story2 = new PlusStory();
        when(plusStoryRepository.findAll()).thenReturn(Arrays.asList(story1, story2));

        // Act
        List<PlusStory> result = plusStoryService.getAllPlusStory();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(story1));
        assertTrue(result.contains(story2));
        verify(plusStoryRepository, times(1)).findAll();
    }

    @Test
    void testGetPlusStory_Success() {
        // Arrange
        long plusStoryId = 1L;
        PlusStory plusStory = new PlusStory();
        when(plusStoryRepository.findById(plusStoryId)).thenReturn(Optional.of(plusStory));

        // Act
        PlusStory result = plusStoryService.getPlusStory(plusStoryId);

        // Assert
        assertNotNull(result);
        assertEquals(plusStory, result);
        verify(plusStoryRepository, times(1)).findById(plusStoryId);
    }

    @Test
    void testGetPlusStory_NotFound() {
        // Arrange
        long plusStoryId = 1L;
        when(plusStoryRepository.findById(plusStoryId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                plusStoryService.getPlusStory(plusStoryId));
        assertEquals("PlusStory not found", exception.getMessage());
        verify(plusStoryRepository, times(1)).findById(plusStoryId);
    }
}
