package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.games.*;
import de.jare.gildeddice.entities.Npc;
import de.jare.gildeddice.entities.character.CharDetails;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.enums.Skill;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.games.storys.Choice;
import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private ChoiceRepository choiceRepository;

    @Mock
    private NpcRepository npcRepository;

    @Mock
    private AiService aiService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStorys() {
        // Arrange
        List<Story> stories = new ArrayList<>();
        stories.add(new Story());
        when(storyRepository.findAll()).thenReturn(stories);

        // Act
        Iterable<Story> result = gameService.getAllStorys();

        // Assert
        assertNotNull(result);
        verify(storyRepository, times(1)).findAll();
    }


    @Test
    void testCreateStory_Success() {
        // Arrange
        StoryCreateDTO dto = new StoryCreateDTO("MAIN", "Title",  1, "PROMPT", new ArrayList<>());
        when(choiceRepository.save(any(Choice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        gameService.createStory(dto);

        // Assert
        verify(storyRepository, times(1)).save(any(Story.class));
    }

    @Test
    void testUpdateStory_Success() {
        // Arrange
        StoryUpdateDTO dto = new StoryUpdateDTO(1L, "MAIN", "Updated Prompt", 2, "Updated Prompt");
        Story existingStory = new Story();
        when(storyRepository.findById(dto.id())).thenReturn(Optional.of(existingStory));

        // Act
        gameService.updateStory(dto);

        // Assert
        assertEquals(dto.title(), existingStory.getTitle());
        verify(storyRepository, times(1)).save(existingStory);
    }


    @Test
    void testUpdateStory_NotFound() {
        // Arrange
        StoryUpdateDTO dto = new StoryUpdateDTO(1L, "MAIN", "Updated Prompt", 2, "Updated Prompt");
        when(storyRepository.findById(dto.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.updateStory(dto));
    }

    @Test
    void testGetAllNpc() {
        // Arrange
        List<Npc> npcs = new ArrayList<>();
        npcs.add(new Npc());
        when(npcRepository.findAll()).thenReturn(npcs);

        // Act
        Iterable<Npc> result = gameService.getAllNpc();

        // Assert
        assertNotNull(result);
        verify(npcRepository, times(1)).findAll();
    }

    @Test
    void testCreateNpc() {
        // Arrange
        String npcName = "Test NPC";

        // Act
        gameService.createNpc(npcName);

        // Assert
        verify(npcRepository, times(1)).save(any(Npc.class));
    }

    @Test
    void testUpdateChoice_Success() {
        // Arrange
        ChoiceUpdateDTO dto = new ChoiceUpdateDTO(1L, "Updated Title", Skill.PLANNING.name(), 10, "Start", "Win", "Lose", "Crit", 1L);
        Choice choice = new Choice();
        Npc npc = new Npc();
        when(choiceRepository.findById(dto.id())).thenReturn(Optional.of(choice));
        when(npcRepository.findById(dto.npcId())).thenReturn(Optional.of(npc));

        // Act
        gameService.updateChoice(dto);

        // Assert
        assertEquals(dto.title(), choice.getTitle());
        verify(choiceRepository, times(1)).save(choice);
    }

    @Test
    void testUpdateChoice_ChoiceNotFound() {
        // Arrange
        ChoiceUpdateDTO dto = new ChoiceUpdateDTO(1L, "Updated Title", Skill.PLANNING.name(), 10, "Start", "Win", "Lose", "Crit", 1L);
        when(choiceRepository.findById(dto.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.updateChoice(dto));
    }
/*
    @Test
    void testGetGamePhase_GameExists() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = new User();
        user.setId(1L);
        Profile profile = new Profile();
        profile.setId(1L);
        CharDetails charDetails = new CharDetails();
        charDetails.setId(1L);
        profile.setCharDetails(charDetails);
        user.setProfile(profile);

        Game game = new Game();
        game.setPhase(1);
        Story story = new Story();
        story.setPrompt("Story Prompt");

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(game);
        when(storyRepository.findByPhase(game.getPhase())).thenReturn(story);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        verify(gameRepository, times(1)).findByUser(user);
        verify(storyRepository, times(1)).findByPhase(game.getPhase());
    }



    @Test
    void testGetGamePhase_NewGame() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = new User();
        Story story = new Story();
        story.setPrompt("New Story Prompt");

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(null);
        when(storyRepository.findByPhase(1)).thenReturn(story);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(storyRepository, times(1)).findByPhase(1);
    }
 */
}
