package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.ai.response.KSuitAiChoicesDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiMessageDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiResponseDTO;
import de.jare.gildeddice.dtos.games.choice.ChoiceCreateDTO;
import de.jare.gildeddice.dtos.games.choice.ChoiceUpdateDTO;
import de.jare.gildeddice.dtos.games.choice.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.choice.GameChoiceResultDTO;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import de.jare.gildeddice.dtos.games.story.StoryCreateDTO;
import de.jare.gildeddice.dtos.games.story.StoryUpdateDTO;
import de.jare.gildeddice.entities.games.storys.Npc;
import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.enums.Skill;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.games.choices.Choice;
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

import java.util.*;

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

    @Mock
    private CharDetailsService charDetailsService;

    @Mock
    private PlusStoryService plusStoryService;

    private StoryUpdateDTO storyUpdateDTO;
    private ChoiceCreateDTO choiceCreateDTO;
    ChoiceUpdateDTO choiceUpdateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        storyUpdateDTO = new StoryUpdateDTO(1L, "MAINX", "title", 2,false, "Updated Prompt",false, false);

        choiceCreateDTO = new ChoiceCreateDTO(
                "Choice Title", "NEGOTIATE", 10, 0, true,"Start Message",
                "Win Message", 100, 50, 200, 0,true, false, false, false, false,
                false, false, false, 5, 3, 2, "Lose Message", 50, 20, 0, 0,false, false,
                false, false, false, false, false, false,2, -1, 0, "Crit Message", 150, 80, 0, 0,true,
                1, 2,4,1L,false
        );

        choiceUpdateDTO = new ChoiceUpdateDTO(
                1L, "Updated Title", Skill.PLANNING.name(), 10, null, true, "Start Message",
                "Win Message", 100, 50, 2.2f, 500, true, false, false, true, false, true, false, false, 1, 5, 10,
                "Lose Message", 80, 40, 1.0f, 200, false, true, false, true, false, false, true, false, 7, 5, 6,
                "Crit Message", 120, 60, 2.0f, 700, true, 4, 9, 12,
                1L
        );

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




    //------ create story -------

    @Test
    void testCreateStory_Success() {
        // Arrange
        StoryCreateDTO storyDto = getStoryCreateDTO();

        Npc npc = new Npc();
        npc.setId(1L);
        when(npcRepository.findById(1L)).thenReturn(Optional.of(npc));
        when(choiceRepository.save(any(Choice.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        gameService.createStory(storyDto);

        // Assert
        verify(npcRepository, times(1)).findById(1L);
        verify(choiceRepository, times(1)).save(any(Choice.class));
        verify(storyRepository, times(1)).save(any(Story.class));
    }

    @Test
    void testCreateStory_NpcNotFound() {
        // Arrange
        StoryCreateDTO storyDto = getStoryCreateDTO();

        when(npcRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                gameService.createStory(storyDto)
        );
        assertEquals("npc not found!", exception.getMessage());
        verify(npcRepository, times(1)).findById(1L);
    }

    //------ Update story ------

    @Test
    void testUpdateStory_Success() {
        // Arrange
        Story existingStory = new Story();
        existingStory.setCategory(Category.MAINL);
        when(storyRepository.findById(storyUpdateDTO.id())).thenReturn(Optional.of(existingStory));

        // Act
        gameService.updateStory(storyUpdateDTO);

        // Assert
        assertEquals(storyUpdateDTO.title(), existingStory.getTitle());
        verify(storyRepository, times(1)).save(existingStory);
    }


    @Test
    void testUpdateStory_NotFound() {
        // Arrange
        when(storyRepository.findById(storyUpdateDTO.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.updateStory(storyUpdateDTO));
    }

    //--- NPC ----

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

    //--- update choices --

    @Test
    void testUpdateChoice_Success() {
        // Arrange

        Choice choice = new Choice();
        Npc npc = new Npc();
        when(choiceRepository.findById(choiceUpdateDTO.id())).thenReturn(Optional.of(choice));
        when(npcRepository.findById(choiceUpdateDTO.npcId())).thenReturn(Optional.of(npc));

        // Act
        gameService.updateChoice(choiceUpdateDTO);

        // Assert
        assertEquals(choiceUpdateDTO.title(), choice.getTitle());
        assertEquals(Skill.valueOf(choiceUpdateDTO.skill()), choice.getSkill());
        assertEquals(choiceUpdateDTO.minDiceValue(), choice.getMinDiceValue());
        assertEquals(choiceUpdateDTO.startMessage(), choice.getStartMessage());

        assertEquals(choiceUpdateDTO.winMessage(), choice.getWinMessage());
        assertEquals(choiceUpdateDTO.winIncomeValue(), choice.getWinIncomeValue());
        assertEquals(choiceUpdateDTO.winOutcomeValue(), choice.getWinOutcomeValue());
        assertEquals(choiceUpdateDTO.winInvestmentPercent(), choice.getWinInvestmentPercent());
        assertEquals(choiceUpdateDTO.winOneTimePayment(), choice.getWinOneTimePayment());
        assertEquals(choiceUpdateDTO.winStudy(), choice.getWinStudy());
        assertEquals(choiceUpdateDTO.winScholarship(), choice.getWinScholarship());
        assertEquals(choiceUpdateDTO.winApprenticeship(), choice.getWinApprenticeship());
        assertEquals(choiceUpdateDTO.winJob(), choice.getWinJob());
        assertEquals(choiceUpdateDTO.winProperty(), choice.getWinProperty());
        assertEquals(choiceUpdateDTO.winRentApartment(), choice.getWinRentApartment());
        assertEquals(choiceUpdateDTO.winCar(), choice.getWinCar());
        assertEquals(choiceUpdateDTO.winDriverLicense(), choice.getWinDriverLicense());
        assertEquals(choiceUpdateDTO.winStressValue(), choice.getWinStressValue());
        assertEquals(choiceUpdateDTO.winSatisfactionValue(), choice.getWinSatisfactionValue());
        assertEquals(choiceUpdateDTO.winHealthValue(), choice.getWinHealthValue());

        assertEquals(choiceUpdateDTO.loseMessage(), choice.getLoseMessage());
        assertEquals(choiceUpdateDTO.loseIncomeValue(), choice.getLoseIncomeValue());
        assertEquals(choiceUpdateDTO.loseOutcomeValue(), choice.getLoseOutcomeValue());
        assertEquals(choiceUpdateDTO.loseInvestmentPercent(), choice.getLoseInvestmentPercent());
        assertEquals(choiceUpdateDTO.loseOneTimePayment(), choice.getLoseOneTimePayment());
        assertEquals(choiceUpdateDTO.loseStudy(), choice.getLoseStudy());
        assertEquals(choiceUpdateDTO.loseScholarship(), choice.getLoseScholarship());
        assertEquals(choiceUpdateDTO.loseApprenticeship(), choice.getLoseApprenticeship());
        assertEquals(choiceUpdateDTO.loseJob(), choice.getLoseJob());
        assertEquals(choiceUpdateDTO.loseProperty(), choice.getLoseProperty());
        assertEquals(choiceUpdateDTO.loseRentApartment(), choice.getLoseRentApartment());
        assertEquals(choiceUpdateDTO.loseCar(), choice.getLoseCar());
        assertEquals(choiceUpdateDTO.loseDriverLicense(), choice.getLoseDriverLicense());
        assertEquals(choiceUpdateDTO.loseStressValue(), choice.getLoseStressValue());
        assertEquals(choiceUpdateDTO.loseSatisfactionValue(), choice.getLoseSatisfactionValue());
        assertEquals(choiceUpdateDTO.loseHealthValue(), choice.getLoseHealthValue());

        assertEquals(choiceUpdateDTO.critMessage(), choice.getCritMessage());
        assertEquals(choiceUpdateDTO.critIncomeValue(), choice.getCritIncomeValue());
        assertEquals(choiceUpdateDTO.critOutcomeValue(), choice.getCritOutcomeValue());
        assertEquals(choiceUpdateDTO.critInvestmentPercent(), choice.getCritInvestmentPercent());
        assertEquals(choiceUpdateDTO.critOneTimePayment(), choice.getCritOneTimePayment());
        assertEquals(choiceUpdateDTO.critScholarship(), choice.getCritScholarship());
        assertEquals(choiceUpdateDTO.critStressValue(), choice.getCritStressValue());
        assertEquals(choiceUpdateDTO.critSatisfactionValue(), choice.getCritSatisfactionValue());
        assertEquals(choiceUpdateDTO.critHealthValue(), choice.getCritHealthValue());

        assertEquals(npc, choice.getNpc());

        verify(choiceRepository, times(1)).save(choice);
    }

    @Test
    void testUpdateChoice_ChoiceNotFound() {
        // Arrange

        when(choiceRepository.findById(choiceUpdateDTO.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.updateChoice(choiceUpdateDTO));
    }

    @Test
    void testGetGamePhase_NewGame_StoryFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfileWithCharDetails();
        Story story = createStoryWithChoices();
        story.setCategory(Category.FATE);

        // Mocking des PlusStoryService
        List<PlusStory> mockPlusStories = List.of(new PlusStory());
        when(plusStoryService.getAllPlusStory()).thenReturn(mockPlusStories);

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.empty());
        when(storyRepository.findByPhase(10)).thenReturn(story);

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
        verify(plusStoryService, times(1)).getAllPlusStory();
    }

    @Test
    void testGetGamePhase_ExistingGame_StoryFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        Game game = new Game();
        game.setPhase(1);
        Story story = createStoryWithChoices();
        story.setCategory(Category.FATE);

        List<PlusStory> mockPlusStories = List.of(new PlusStory());
        when(plusStoryService.getAllPlusStory()).thenReturn(mockPlusStories);

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));
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
        verify(plusStoryService, times(1)).getAllPlusStory();
    }

    @Test
    void testGetGamePhase_StoryNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        user.getProfile().setCharDetails(new CharDetails());
        Game game = new Game();
        game.setPhase(2);

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));
        when(storyRepository.findByPhase(game.getPhase())).thenReturn(null);

        // Act
        GamePhaseDTO result = gameService.getGamePhase(auth);

        // Assert
        assertNotNull(result);
        assertEquals("Story not found for phase 2", result.intro());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testResetGame_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
        Game game = new Game();
        game.setPhase(5);
        game.setGameLost(true);

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));

        // Act
        gameService.resetGame(auth);

        // Assert
        assertEquals(10, game.getPhase(), "Die Phase sollte auf 10 zurückgesetzt sein.");
        assertFalse(game.isGameLost(), "Das Spiel sollte nicht verloren sein.");
        verify(gameRepository, times(1)).save(game);
        verify(charDetailsService, times(1)).resetChar(auth);
    }

    @Test
    void testResetGame_GameNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();

        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.resetGame(auth));
        assertEquals("GameNotFound", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
        verify(charDetailsService, times(0)).resetChar(auth);
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
        npc.setFilename("file.png");
        choice.setNpc(npc);

        when(choiceRepository.findById(choice.getId())).thenReturn(Optional.of(choice));
        // Act
        GameChoiceDTO result = gameService.getChoiceDetails(choice.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Test Choice", result.title());
        assertEquals(Skill.INTELLIGENCE.name(), result.skill());
        assertEquals("Start Text", result.startMessage());
        assertEquals("NPC", result.npcName());
        assertEquals("file.png", result.npcFilename());
    }

    @Test
    void testGetChoiceDetails_ChoiceNotFound() {
        // Arrange
        when(choiceRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.getChoiceDetails(1L));
    }


    @Test
    void testPlayChoice_GameLost() {
        // Arrange
        long choiceId = 1L;
        int diceResult = 5; // Niedriger Würfelwert, damit die Chance hoch ist, zu verlieren
        Authentication auth = mock(Authentication.class);

        User user = createUserWithProfileWithCharDetails();
        Game game = new Game();
        game.setGameLost(false);

        Choice choice = new Choice();
        choice.setSkill(Skill.INTELLIGENCE);
        choice.setMinDiceValue(15);

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));

        when(charDetailsService.setCharacterStatusLvls(anyLong(), anyInt(), any(), any(), any())).thenReturn(true);

        // Act
        GameChoiceResultDTO result = gameService.playChoice(choiceId, diceResult, auth);

        // Assert
        assertNotNull(result);
        assertTrue(result.gameLost());
        verify(gameRepository, times(1)).save(game);
        assertTrue(game.isGameLost());
    }

    @Test
    void testPlayChoice_ChoiceNotFound() {
        // Arrange
        long choiceId = 1L;
        int diceResult = 10;
        Authentication auth = mock(Authentication.class);

        User user = createUserWithProfileWithCharDetails();
        Game game = new Game();
        game.setGameLost(false);

        when(userService.getUser(auth)).thenReturn(user);

        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.playChoice(choiceId, diceResult, auth));
        assertEquals("Choice not found!", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testPlayChoice_GameNotFound() {
        // Arrange
        long choiceId = 1L;
        int diceResult = 10;
        Authentication auth = mock(Authentication.class);

        User user = createUserWithProfileWithCharDetails();
        Choice choice = new Choice();

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.playChoice(choiceId, diceResult, auth));
        assertEquals("Game not found", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testPlayChoice_CharDetailsNotFound() {
        // Arrange
        long choiceId = 1L;
        int diceResult = 10;
        Authentication auth = mock(Authentication.class);

        User user = createUserWithoutCharDetails();
        Choice choice = new Choice();

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
        when(userService.getUser(auth)).thenReturn(user);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.playChoice(choiceId, diceResult, auth));
        assertEquals("Character was not fund", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }


//---------------



    private StoryCreateDTO getStoryCreateDTO() {

        return new StoryCreateDTO(
                "MAINL", "Story Title", 1, false, false,"Prompt", true, List.of(choiceCreateDTO)
        );
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

    private User createUserWithoutCharDetails() {
        User user = new User();
        Profile profile = new Profile();
        profile.setUsername("TestUser");
        user.setProfile(profile);
        return user;
    }

    private User createUserWithProfileWithCharDetails() {
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
        story.setPhase(10);
        story.setPrompt("Story Prompt");
        List<Choice> choices = new ArrayList<>();
        Choice choice1 = new Choice();
        choice1.setTitle("Choice 1");
        choices.add(choice1);
        story.setChoices(choices);
        story.setPhaseEnd(false);
        return story;
    }


}

