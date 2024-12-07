package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.ai.response.KSuitAiChoicesDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiMessageDTO;
import de.jare.gildeddice.dtos.ai.response.KSuitAiResponseDTO;
import de.jare.gildeddice.dtos.games.choice.ChoiceUpdateDTO;
import de.jare.gildeddice.dtos.games.game.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.game.GameChoiceResultDTO;
import de.jare.gildeddice.dtos.games.game.GamePhaseDTO;
import de.jare.gildeddice.dtos.games.story.StoryCreateDTO;
import de.jare.gildeddice.dtos.games.story.StoryUpdateDTO;
import de.jare.gildeddice.entities.games.storys.Npc;
import de.jare.gildeddice.entities.character.CharDetails;
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

    @Mock
    private CharDetailsService charDetailsService;

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
        StoryCreateDTO dto = new StoryCreateDTO("MAIN", "Title",  1, false, "PROMPT",false, new ArrayList<>());
        when(choiceRepository.save(any(Choice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        gameService.createStory(dto);

        // Assert
        verify(storyRepository, times(1)).save(any(Story.class));
    }

    @Test
    void testUpdateStory_Success() {
        // Arrange
        StoryUpdateDTO dto = new StoryUpdateDTO(1L, "MAIN", "Updated Prompt", 2, false, false, "Updated Prompt");
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
        StoryUpdateDTO dto = new StoryUpdateDTO(1L, "MAIN", "Updated Prompt", 2,false, false, "Updated Prompt");
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
        ChoiceUpdateDTO dto = new ChoiceUpdateDTO(
                1L, "Updated Title", Skill.PLANNING.name(), 10, "Start Message",
                "Win Message", 100, 50, 2.2f, 500, true, false, false, true, false, true, false, 1, 5, 10,
                "Lose Message", 80, 40, 1.0f, 200, false, true, false, true, false, false, true, 7, 5, 6,
                "Crit Message", 120, 60, 2.0f, 700, true, 4, 9, 12,
                1L
        );
        Choice choice = new Choice();
        Npc npc = new Npc();
        when(choiceRepository.findById(dto.id())).thenReturn(Optional.of(choice));
        when(npcRepository.findById(dto.npcId())).thenReturn(Optional.of(npc));

        // Act
        gameService.updateChoice(dto);

        // Assert
        assertEquals(dto.title(), choice.getTitle());
        assertEquals(Skill.valueOf(dto.skill()), choice.getSkill());
        assertEquals(dto.minDiceValue(), choice.getMinDiceValue());
        assertEquals(dto.startMessage(), choice.getStartMessage());

        // Verifikation der Gewinn-Eigenschaften
        assertEquals(dto.winMessage(), choice.getWinMessage());
        assertEquals(dto.winIncomeValue(), choice.getWinIncomeValue());
        assertEquals(dto.winOutcomeValue(), choice.getWinOutcomeValue());
        assertEquals(dto.winInvestmentPercent(), choice.getWinInvestmentPercent());
        assertEquals(dto.winOneTimePayment(), choice.getWinOneTimePayment());
        assertEquals(dto.winStudy(), choice.getWinStudy());
        assertEquals(dto.winScholarship(), choice.getWinScholarship());
        assertEquals(dto.winApprenticeship(), choice.getWinApprenticeship());
        assertEquals(dto.winJob(), choice.getWinJob());
        assertEquals(dto.winProperty(), choice.getWinProperty());
        assertEquals(dto.winRentApartment(), choice.getWinRentApartment());
        assertEquals(dto.winCar(), choice.getWinCar());
        assertEquals(dto.winStressValue(), choice.getWinStressValue());
        assertEquals(dto.winSatisfactionValue(), choice.getWinSatisfactionValue());
        assertEquals(dto.winHealthValue(), choice.getWinHealthValue());

        // Verifikation der Verlust-Eigenschaften
        assertEquals(dto.loseMessage(), choice.getLoseMessage());
        assertEquals(dto.loseIncomeValue(), choice.getLoseIncomeValue());
        assertEquals(dto.loseOutcomeValue(), choice.getLoseOutcomeValue());
        assertEquals(dto.loseInvestmentPercent(), choice.getLoseInvestmentPercent());
        assertEquals(dto.loseOneTimePayment(), choice.getLoseOneTimePayment());
        assertEquals(dto.loseStudy(), choice.getLoseStudy());
        assertEquals(dto.loseScholarship(), choice.getLoseScholarship());
        assertEquals(dto.loseApprenticeship(), choice.getLoseApprenticeship());
        assertEquals(dto.loseJob(), choice.getLoseJob());
        assertEquals(dto.loseProperty(), choice.getLoseProperty());
        assertEquals(dto.loseRentApartment(), choice.getLoseRentApartment());
        assertEquals(dto.loseCar(), choice.getLoseCar());
        assertEquals(dto.loseStressValue(), choice.getLoseStressValue());
        assertEquals(dto.loseSatisfactionValue(), choice.getLoseSatisfactionValue());
        assertEquals(dto.loseHealthValue(), choice.getLoseHealthValue());

        // Verifikation der kritischen Eigenschaften
        assertEquals(dto.critMessage(), choice.getCritMessage());
        assertEquals(dto.critIncomeValue(), choice.getCritIncomeValue());
        assertEquals(dto.critOutcomeValue(), choice.getCritOutcomeValue());
        assertEquals(dto.critInvestmentPercent(), choice.getCritInvestmentPercent());
        assertEquals(dto.critOneTimePayment(), choice.getCritOneTimePayment());
        assertEquals(dto.critScholarship(), choice.getCritScholarship());
        assertEquals(dto.critStressValue(), choice.getCritStressValue());
        assertEquals(dto.critSatisfactionValue(), choice.getCritSatisfactionValue());
        assertEquals(dto.critHealthValue(), choice.getCritHealthValue());

        assertEquals(npc, choice.getNpc());

        // Verifikation, dass die Repository-Methode `save` aufgerufen wurde
        verify(choiceRepository, times(1)).save(choice);
    }

    @Test
    void testUpdateChoice_ChoiceNotFound() {
        // Arrange
        ChoiceUpdateDTO dto = new ChoiceUpdateDTO(
                1L, "Updated Title", Skill.PLANNING.name(), 10, "Start Message",
                "Win Message", 100, 50, 2.2f, 500, true, false, false, true, false, true, false, 1, 5, 10,
                "Lose Message", 80, 40, 1.5f, 200, false, true, false, true, false, false, true, 7, 5, 6,
                "Crit Message", 120, 60, 2.5f, 700, true, 4, 9, 12,
                1L
        );
        when(choiceRepository.findById(dto.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> gameService.updateChoice(dto));
    }

    @Test
    void testGetGamePhase_NewGame_StoryFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfileWithCharDetails();
        Story story = createStoryWithChoices();
        story.setCategory(Category.FATE);

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
    }

    @Test
    void testGetGamePhase_StoryNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        User user = createUserWithProfile();
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

//    @Test
//    void testPlayChoice_Success_Win() {
//        // Arrange
//        long choiceId = 1L;
//        int diceResult = 15;
//        Authentication auth = mock(Authentication.class);
//
//        User user = createUserWithProfileWithCharDetails();
//        Game game = new Game();
//        game.setGameLost(false);
//
//        Choice choice = new Choice();
//
//        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
//        when(userService.getUser(auth)).thenReturn(user);
//        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));
//
//        // Act
//        GameChoiceResultDTO result = gameService.playChoice(choiceId, diceResult, auth);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isWin() || result.isGameLost() == false); // Sicherstellen, dass das Ergebnis entweder gewonnen oder verloren ist
//        verify(gameRepository, times(1)).save(game);
//    }

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
        choice.setMinDiceValue(15); // Höherer Wert, sodass der Spieler wahrscheinlich verliert

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
        when(userService.getUser(auth)).thenReturn(user);
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));

        // Mock für CharDetailsService, um sicherzustellen, dass das Spiel verloren wird
        when(charDetailsService.setCharacterStatusLvls(anyLong(), anyInt(), any(), any(), any())).thenReturn(true);

        // Act
        GameChoiceResultDTO result = gameService.playChoice(choiceId, diceResult, auth);

        // Assert
        assertNotNull(result);
        assertTrue(result.gameLost()); // Sicherstellen, dass das Spiel als verloren markiert wird
        verify(gameRepository, times(1)).save(game);
        assertTrue(game.isGameLost()); // Überprüfen, dass das Spiel als verloren markiert ist
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

        // Mock das UserService, um einen Benutzer zurückzugeben
        when(userService.getUser(auth)).thenReturn(user);
        // Mock das GameRepository, um ein bestehendes Spiel zurückzugeben
        when(gameRepository.findByUsername(user.getProfile().getUsername())).thenReturn(Optional.of(game));
        // Mock das ChoiceRepository, damit keine Choice gefunden wird
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

        User user = createUserWithoutCharDetails(); // Benutzer ohne CharDetails
        Choice choice = new Choice();

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));
        when(userService.getUser(auth)).thenReturn(user);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.playChoice(choiceId, diceResult, auth));
        assertEquals("Character was not fund", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }

















//---------------

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

