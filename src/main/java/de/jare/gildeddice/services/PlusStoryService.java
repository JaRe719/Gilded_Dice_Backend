package de.jare.gildeddice.services;

import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.repositories.PlusStoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlusStoryService {

    private PlusStoryRepository plusStoryRepository;

    public PlusStoryService(PlusStoryRepository plusStoryRepository) {
        this.plusStoryRepository = plusStoryRepository;
    }

    public List<PlusStory> getAllPlusStory() {
        return plusStoryRepository.findAll();
    }

}
