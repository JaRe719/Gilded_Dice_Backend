package de.jare.gildeddice.services;

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
import de.jare.gildeddice.mapper.GameMapper;
import de.jare.gildeddice.repositories.ChoiceRepository;
import de.jare.gildeddice.repositories.GameRepository;
import de.jare.gildeddice.repositories.NpcRepository;
import de.jare.gildeddice.repositories.StoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private GameRepository gameRepository;
    private StoryRepository storyRepository;
    private ChoiceRepository choiceRepository;
    private NpcRepository npcRepository;

    private AiService aiService;
    private UserService userService;
    private CharDetailsService charDetailsService;

    private HighScoreService highScoreService;


    public GameService(GameRepository gameRepository, StoryRepository storyRepository, ChoiceRepository choiceRepository, NpcRepository npcRepository, AiService aiService, UserService userService, CharDetailsService charDetailsService, HighScoreService highScoreService) {
        this.gameRepository = gameRepository;
        this.storyRepository = storyRepository;
        this.choiceRepository = choiceRepository;
        this.npcRepository = npcRepository;
        this.aiService = aiService;
        this.userService = userService;
        this.charDetailsService = charDetailsService;
        this.highScoreService = highScoreService;
    }

    public Iterable<Story> getAllStorys() {
        return storyRepository.findAll();
    }

    public void createStory(StoryCreateDTO dto) {
        Story story = new Story();
        story.setCategory(Category.valueOf(dto.category()));
        story.setTitle(dto.title());
        story.setPhase(dto.phase());
        story.setPrompt(dto.prompt());
        story.setPhaseEnd(dto.phaseEnd());
        story.setChoices(createStoryList(dto.choices()));
        storyRepository.save(story);
    }

    private List<Choice> createStoryList(List<ChoiceCreateDTO> choices) {
        List<Choice> choiceEntities = new ArrayList<>();
        for (ChoiceCreateDTO choice : choices) {
            Choice choiceEntity = new Choice();
            choiceEntity.setTitle(choice.title());
            choiceEntity.setSkill(Skill.valueOf(choice.skill()));
            choiceEntity.setMinDiceValue(choice.minDiceValue());
            choiceEntity.setStartMessage(choice.startMessage());

            choiceEntity.setWinMessage(choice.winMessage());
            choiceEntity.setWinIncomeValue(choice.winIncomeValue());
            choiceEntity.setWinOutcomeValue(choice.winOutcomeValue());
            choiceEntity.setWinOneTimePayment(choice.winOneTimePayment());

            choiceEntity.setWinStudy(choice.winStudy());
            choiceEntity.setWinScholarship(choice.winScholarship());
            choiceEntity.setWinApprenticeship(choice.winApprenticeship());
            choiceEntity.setWinJob(choice.winJob());
            choiceEntity.setWinProperty(choice.winProperty());
            choiceEntity.setWinRentApartment(choice.winRentApartment());
            choiceEntity.setWinCar(choice.winCar());

            choiceEntity.setWinStressValue(choice.winStressValue());
            choiceEntity.setWinSatisfactionValue(choice.winSatisfactionValue());
            choiceEntity.setWinHealthValue(choice.winHealthValue());


            choiceEntity.setLoseMessage(choice.loseMessage());
            choiceEntity.setLoseIncomeValue(choice.loseIncomeValue());
            choiceEntity.setLoseOutcomeValue(choice.loseOutcomeValue());
            choiceEntity.setLoseOneTimePayment(choice.loseOneTimePayment());

            choiceEntity.setLoseStudy(choice.loseStudy());
            choiceEntity.setLoseScholarship(choice.loseScholarship());
            choiceEntity.setLoseApprenticeship(choice.loseApprenticeship());
            choiceEntity.setLoseJob(choice.loseJob());
            choiceEntity.setLoseProperty(choice.loseProperty());
            choiceEntity.setLoseRentApartment(choice.loseRentApartment());
            choiceEntity.setLoseCar(choice.loseCar());

            choiceEntity.setLoseStressValue(choice.loseStressValue());
            choiceEntity.setLoseSatisfactionValue(choice.loseSatisfactionValue());
            choiceEntity.setLoseHealthValue(choice.loseHealthValue());


            choiceEntity.setCritMessage(choice.critMessage());
            choiceEntity.setCritIncomeValue(choice.critIncomeValue());
            choiceEntity.setCritOutcomeValue(choice.critOutcomeValue());
            choiceEntity.setCritOneTimePayment(choice.critOneTimePayment());

            choiceEntity.setCritScholarship(choice.critScholarship());

            choiceEntity.setCritStressValue(choice.critStressValue());
            choiceEntity.setCritSatisfactionValue(choice.critSatisfactionValue());
            choiceEntity.setCritHealthValue(choice.critHealthValue());


            choiceEntity.setNpc(npcRepository.findById(choice.npcId()).orElseThrow(() -> new EntityNotFoundException("npc not found!")));

            choiceEntity = choiceRepository.save(choiceEntity);
            choiceEntities.add(choiceEntity);
        }

        return choiceEntities;
    }

    public void updateStory(StoryUpdateDTO dto) {
        Story story = storyRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Story not found!"));
        story.setTitle(dto.title());
        story.setPhase(dto.phase());
        story.setPhaseEnd(dto.phaseEnd());
        story.setPrompt(dto.prompt());
        storyRepository.save(story);
    }

    public Iterable<Npc> getAllNpc() {
        return npcRepository.findAll();
    }

    public void createNpc(String npcName, String filename) {
        Npc npc = new Npc();
        npc.setName(npcName);
        npc.setFilename(filename);
        npcRepository.save(npc);
    }

    public Iterable<Choice> getAllChoice() {
        return choiceRepository.findAll();
    }

    public void updateChoice(ChoiceUpdateDTO dto) {
        Choice choiceEntity = choiceRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));

        choiceEntity.setTitle(dto.title());
        choiceEntity.setSkill(Skill.valueOf(dto.skill()));
        choiceEntity.setMinDiceValue(dto.minDiceValue());
        choiceEntity.setStartMessage(dto.startMessage());

        choiceEntity.setWinMessage(dto.winMessage());
        choiceEntity.setWinIncomeValue(dto.winIncomeValue());
        choiceEntity.setWinOutcomeValue(dto.winOutcomeValue());
        choiceEntity.setWinOneTimePayment(dto.winOneTimePayment());
        choiceEntity.setWinInvestmentPercent(dto.winInvestmentPercent());

        choiceEntity.setWinStudy(dto.winStudy());
        choiceEntity.setWinScholarship(dto.winScholarship());
        choiceEntity.setWinApprenticeship(dto.winApprenticeship());
        choiceEntity.setWinJob(dto.winJob());
        choiceEntity.setWinProperty(dto.winProperty());
        choiceEntity.setWinRentApartment(dto.winRentApartment());
        choiceEntity.setWinCar(dto.winCar());

        choiceEntity.setWinStressValue(dto.winStressValue());
        choiceEntity.setWinSatisfactionValue(dto.winSatisfactionValue());
        choiceEntity.setWinHealthValue(dto.winHealthValue());


        choiceEntity.setLoseMessage(dto.loseMessage());
        choiceEntity.setLoseIncomeValue(dto.loseIncomeValue());
        choiceEntity.setLoseOutcomeValue(dto.loseOutcomeValue());
        choiceEntity.setLoseOneTimePayment(dto.loseOneTimePayment());
        choiceEntity.setLoseInvestmentPercent(dto.loseInvestmentPercent());

        choiceEntity.setLoseStudy(dto.loseStudy());
        choiceEntity.setLoseScholarship(dto.loseScholarship());
        choiceEntity.setLoseApprenticeship(dto.loseApprenticeship());
        choiceEntity.setLoseJob(dto.loseJob());
        choiceEntity.setLoseProperty(dto.loseProperty());
        choiceEntity.setLoseRentApartment(dto.loseRentApartment());
        choiceEntity.setLoseCar(dto.loseCar());

        choiceEntity.setLoseStressValue(dto.loseStressValue());
        choiceEntity.setLoseSatisfactionValue(dto.loseSatisfactionValue());
        choiceEntity.setLoseHealthValue(dto.loseHealthValue());


        choiceEntity.setCritMessage(dto.critMessage());
        choiceEntity.setCritIncomeValue(dto.critIncomeValue());
        choiceEntity.setCritOutcomeValue(dto.critOutcomeValue());
        choiceEntity.setCritOneTimePayment(dto.critOneTimePayment());
        choiceEntity.setCritInvestmentPercent(dto.critInvestmentPercent());

        choiceEntity.setCritScholarship(dto.critScholarship());

        choiceEntity.setCritStressValue(dto.critStressValue());
        choiceEntity.setCritSatisfactionValue(dto.critSatisfactionValue());
        choiceEntity.setCritHealthValue(dto.critHealthValue());


        choiceEntity.setNpc(npcRepository.findById(dto.npcId()).orElseThrow(() -> new EntityNotFoundException("npc not found!")));

        choiceRepository.save(choiceEntity);
    }
    //------


    public GamePhaseDTO getGamePhase(Authentication auth) {
        User user = userService.getUser(auth);
        if (user.getProfile().getCharDetails() == null) throw new IllegalStateException("no Char");
        Optional<Game> existingGame = gameRepository.findByUsername(user.getProfile().getUsername());

        Game game;

        if (existingGame.isPresent()) {
             game = existingGame.get();
        } else {
            game = new Game();
            game.setUsername(user.getProfile().getUsername());
            game.setPhase(10);
        }
        if (game.isGameLost()) throw new IllegalStateException("Game is lost");

        Story story = storyRepository.findByPhase(game.getPhase());
        if (story == null) {
            GamePhaseDTO error = new GamePhaseDTO("null", "Story not found for phase " + game.getPhase(), new ArrayList<>());
            game.setPhase(10);
            gameRepository.save(game);
            return error;
        }

        String finalPrompt = createCompletedPrompt(story, user);
        KSuitAiResponseDTO responseDTO = aiService.callApi(finalPrompt);

        if (story.isPhaseEnd()) {
            game.setPhase(((game.getPhase() + 9) / 10) * 10);

        } else game.setPhase(game.getPhase() + 1);

        saveHighScoreWhenGameIsEnd(user.getProfile(), game, story);

        gameRepository.save(game);
        return GameMapper.toGamePhaseDTO(story.getCategory(), responseDTO.choices().getFirst().message().content(), story.getChoices());
    }

    private void saveHighScoreWhenGameIsEnd(Profile profile, Game game, Story story) {
        if (game.isGameLost()) {
            int highScore = profile.getCharDetails().getMoney();
            if (profile.getHighScore() < highScore) {
                userService.saveHighScore(profile, highScore);
                highScoreService.saveHighScore(profile);
            }
        }
    }

    public void resetGame(Authentication auth) {
        User user = userService.getUser(auth);
        Game game = gameRepository.findByUsername(user.getProfile().getUsername()).orElseThrow(() -> new EntityNotFoundException("GameNotFound"));

        game.setPhase(10);
        game.setGameLost(false);

        gameRepository.save(game);

        charDetailsService.resetChar(auth);
    }


    private String createCompletedPrompt(Story story, User user) {
        String username = user.getProfile().getUsername();
        CharDetails charDetails = user.getProfile().getCharDetails();

        StringBuilder finalPrompt = new StringBuilder("Erstelle ein kurzes (2-3 sätze max) und in deutsch verfasstes, individuellen text für folgendes PnP-Szenarion basierend auf den folgenden informationen: ");
        finalPrompt.append("charaktername: ").append(username);
        finalPrompt.append(", Szenario: ").append(story.getPrompt());
        finalPrompt.append(", Endscheidung: ");
        for (Choice choice : story.getChoices()) finalPrompt.append(choice.getTitle());
        finalPrompt.append(", Ton: Das Szenario ist ein teil einer gesamtgeschichte, es soll realistisch sein. Den Spieler dutzt. gebe kurze tipps für die finanzielle und zeitliche aussicht, halte dich möglichst kurz und bitte dich nicht zur hilfe an");
        if (story.getPhase() != 10) finalPrompt.append("ladde die Begrüßung weg und steig gleich in das Szenario ein");
        return finalPrompt.toString();
    }

    public GameChoiceDTO getChoiceDetails(long choiceId) {
        Choice choice = choiceRepository.findById(choiceId).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        return GameMapper.toGameChoiceDTO(choice);
    }


    public GameChoiceResultDTO playChoice(long choiceId, int diceResult, Authentication auth) {
        User user = userService.getUser(auth);
        CharDetails charDetails = user.getProfile().getCharDetails();
        if (charDetails == null) throw new EntityNotFoundException("Character was not fund");

        Game game = gameRepository.findByUsername(user.getProfile().getUsername()).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        Choice choice = choiceRepository.findById(choiceId).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));

        MinValueToWinDTO finalMinValueToWin = calculateFinalMinResultToWin(choice, diceResult, charDetails);
        int choiceResult = checkResult(diceResult, finalMinValueToWin.value());
        String responseMessage = compareResultForMessage(choiceResult, choice);

        boolean gameLost = executeChoiceResult(choice, choiceResult, user.getProfile());


        if (gameLost) {
            game.setGameLost(true);
        }

        gameRepository.save(game);
        return new GameChoiceResultDTO(
                choiceResult >= 0,
                gameLost,
                responseMessage,
                finalMinValueToWin.calculation()
        );

    }

    private boolean executeChoiceResult(Choice choice, int choiceResult, Profile userProfile) {
        Game game = gameRepository.findByUsername(userProfile.getUsername()).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        boolean gameLost = false;

        switch (choiceResult) {
            case 1: //critical
                gameLost = charDetailsService.setCharacterStatusLvls(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getCritStressValue(),
                        choice.getCritSatisfactionValue(),
                        choice.getCritHealthValue()
                );

                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getCritIncomeValue(),
                        choice.getCritOutcomeValue(),
                        choice.getCritOneTimePayment()
                );
                charDetailsService.setInventoryByChoice(
                        userProfile.getId(),
                        choice.getWinStudy(),
                        choice.getCritScholarship(),
                        choice.getWinApprenticeship(),
                        choice.getWinJob(),
                        choice.getWinProperty(),
                        choice.getWinRentApartment(),
                        choice.getWinCar()
                );

                break;
            case 0: //win
                gameLost = charDetailsService.setCharacterStatusLvls(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getWinStressValue(),
                        choice.getWinSatisfactionValue(),
                        choice.getWinHealthValue()
                );
                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getWinIncomeValue(),
                        choice.getWinOutcomeValue(),
                        choice.getWinOneTimePayment()
                );
                charDetailsService.setInventoryByChoice(
                        userProfile.getId(),
                        choice.getWinStudy(),
                        choice.getWinScholarship(),
                        choice.getWinApprenticeship(),
                        choice.getWinJob(),
                        choice.getWinProperty(),
                        choice.getWinRentApartment(),
                        choice.getWinCar()
                );
                break;
            case -1: //lose
                gameLost = charDetailsService.setCharacterStatusLvls(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getLoseStressValue(),
                        choice.getLoseSatisfactionValue(),
                        choice.getLoseHealthValue()
                );
                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
                        game.getPhase(),
                        choice.getLoseIncomeValue(),
                        choice.getLoseOutcomeValue(),
                        choice.getLoseOneTimePayment()
                );
                charDetailsService.setInventoryByChoice(
                        userProfile.getId(),
                        choice.getLoseStudy(),
                        choice.getLoseScholarship(),
                        choice.getLoseApprenticeship(),
                        choice.getLoseJob(),
                        choice.getLoseProperty(),
                        choice.getLoseRentApartment(),
                        choice.getLoseCar()
                );
                break;
        }
        return gameLost;
    }


    private String compareResultForMessage(int choiceResult, Choice choice) {
        String resultMessage;

        if (choiceResult == 1) resultMessage = choice.getCritMessage();
        else if (choiceResult == 0) resultMessage = choice.getWinMessage();
        else resultMessage = choice.getLoseMessage();

        return resultMessage;
    }


    private int checkResult(int diceResult, int minValueToWin) {

        if (diceResult == 20) return 1;
        else if (diceResult > minValueToWin) return 0;
        else return -1;
    }

    private MinValueToWinDTO calculateFinalMinResultToWin(Choice choice, int diceResult, CharDetails character) {
        int finalMinResultToWin = choice.getMinDiceValue();

        int handicap = character.getHandicap();

        int skillValue = switch (choice.getSkill()) {
            case INTELLIGENCE -> character.getIntelligence();
            case NEGOTIATE -> character.getNegotiate();
            case ABILITY -> character.getAbility();
            case PLANNING -> character.getPlanning();
            case STAMINA -> character.getStamina();
        };

        finalMinResultToWin = finalMinResultToWin - skillValue - handicap;

        String calcPathGoal = "eigentliche Gewinnschwelle: " + choice.getMinDiceValue()
                + " - " + choice.getSkill().getSkillname() + ": " + skillValue
                + " - Handicap: " + handicap + " = " + finalMinResultToWin
                + " Würfelwert: " + diceResult;

        return new MinValueToWinDTO(finalMinResultToWin, calcPathGoal);
    }
}
