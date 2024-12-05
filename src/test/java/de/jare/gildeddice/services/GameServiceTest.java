package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.ai.response.KSuitAiChoicesDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiMessageDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiResponseDTO;
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
import static org.mockito.BDDMockito.then;
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
        String filename = "npc.png";

        // Act
        gameService.createNpc(npcName, filename);

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

    @Test
    void testGetGamePhase_NewGame_StoryFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        Story story = createStoryWithChoices();

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(null);
        when(storyRepository.findByPhase(1)).thenReturn(story);

        KSuitAiMessageDTO messageDTO = new KSuitAiMessageDTO("assistant", "Response");
        KSuitAiChoicesDTO choicesDTO = new KSuitAiChoicesDTO(0, messageDTO, null, "stop");
        KSuitAiResponseDTO responseDTO = new KSuitAiResponseDTO("gpt-model", "id123", "chat.completion", "fingerprint", System.currentTimeMillis(), List.of(choicesDTO));
        when(aiService.callApi(anyString())).thenReturn(responseDTO);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        assertEquals("Response", result.intro());
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(aiService, times(1)).callApi(anyString());
    }

    @Test
    void testGetGamePhase_ExistingGame_StoryFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        Game game = new Game();
        game.setPhase(1);
        Story story = createStoryWithChoices();

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(game);
        when(storyRepository.findByPhase(1)).thenReturn(story);

        KSuitAiMessageDTO messageDTO = new KSuitAiMessageDTO("assistant", "Response");
        KSuitAiChoicesDTO choicesDTO = new KSuitAiChoicesDTO(0, messageDTO, null, "stop");
        KSuitAiResponseDTO responseDTO = new KSuitAiResponseDTO("gpt-model", "id123", "chat.completion", "fingerprint", System.currentTimeMillis(), List.of(choicesDTO));
        when(aiService.callApi(anyString())).thenReturn(responseDTO);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        assertEquals("Response", result.intro());
        verify(gameRepository, times(1)).save(game);
        verify(aiService, times(1)).callApi(anyString());
    }

    @Test
    void testGetGamePhase_StoryNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        Game game = new Game();
        game.setPhase(2);

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUser(user)).thenReturn(game);
        when(storyRepository.findByPhase(game.getPhase())).thenReturn(null);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        assertEquals("Story not found for phase 2", result.intro());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testGetChoiceDetails_Success() {
        // Arrange
        Choice choice = new Choice();
        choice.setTitle("Test Choice");
        choice.setSkill(Skill.INTELLIGENCE);
        choice.setMinDiceValue(10);
        choice.setStartMessage("Start Text");
        choice.setWinMessage("Win Text");
        choice.setLoseMessage("Lose Text");
        choice.setCritMessage("Crit Text");
        Npc npc = new Npc();
        npc.setId(1L);
        npc.setName("NPC");
        choice.setNpc(npc);

        when(choiceRepository.findById(choice.getId())).thenReturn(Optional.of(choice));
        // Act
        GameChoiceDTO result = gameService.getChoiceDetails(choice.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Test Choice", result.title());
        assertEquals(Skill.INTELLIGENCE.name(), result.skill());
        assertEquals(10, result.minDiceValue());
        assertEquals("Start Text", result.startMessage());
        assertEquals("NPC", result.npc());
    }

    @Test
    void testGetChoiceDetails_ChoiceNotFound() {
        // Arrange
        when(choiceRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.getChoiceDetails(1L));
    }





    private User createUserWithProfile() {
        User user = new User();
        Profile profile = new Profile();
        profile.setUsername("TestUser");
        CharDetails charDetails = new CharDetails();
        profile.setCharDetails(charDetails);
        user.setProfile(profile);
        return user;
    }

    private Story createStoryWithChoices() {
        Story story = new Story();
        story.setPhase(1);
        story.setPrompt("Story Prompt");
        List<Choice> choices = new ArrayList<>();
        Choice choice1 = new Choice();
        choice1.setTitle("Choice 1");
        choices.add(choice1);
        story.setChoices(choices);
        return story;
    }
}

