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

@Service
public class GameService {

    private GameRepository gameRepository;
    private StoryRepository storyRepository;
    private ChoiceRepository choiceRepository;
    private NpcRepository npcRepository;

    private AiService aiService;
    private UserService userService;
    private CharDetailsService charDetailsService;


    public GameService(GameRepository gameRepository, StoryRepository storyRepository, ChoiceRepository choiceRepository, NpcRepository npcRepository, AiService aiService, UserService userService, CharDetailsService charDetailsService) {
        this.gameRepository = gameRepository;
        this.storyRepository = storyRepository;
        this.choiceRepository = choiceRepository;
        this.npcRepository = npcRepository;
        this.aiService = aiService;
        this.userService = userService;
        this.charDetailsService = charDetailsService;
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
        Game game = gameRepository.findByUsername(user.getProfile().getUsername());

        if (game == null) {
            game = new Game();
            game.setUsername(user.getProfile().getUsername());
            game.setPhase(1);
        }

        Story story = storyRepository.findByPhase(game.getPhase());
        if (story == null) {
            GamePhaseDTO error = new GamePhaseDTO("Story not found for phase " + game.getPhase(), new ArrayList<>());
            game.setPhase(1);
            gameRepository.save(game);
            return error;
        }

        String finalPrompt = createCompletedPrompt(story, user);
        System.out.println(finalPrompt);
        KSuitAiResponseDTO responseDTO = aiService.callApi(finalPrompt);
        System.out.println(responseDTO);

        game.setPhase(game.getPhase() + 1);
        gameRepository.save(game);
        return GameMapper.toGamePhaseDTO(responseDTO.choices().getFirst().message().content(), story.getChoices());
    }

    private String createCompletedPrompt(Story story, User user) {
        String username = user.getProfile().getUsername();
        CharDetails charDetails = user.getProfile().getCharDetails();

        StringBuilder finalPrompt = new StringBuilder("Erstelle ein kurzes (2-3 sätze max) und in deutsch verfasstes, individuelles pnp-intro für folgendes Szenarion, basierend auf den folgenden informationen: ");
        finalPrompt.append("charaktername: ").append(username);
        finalPrompt.append(", Szenario: ").append(story.getPrompt());
        finalPrompt.append(", Endscheidung: ");
        for (Choice choice : story.getChoices()) finalPrompt.append(choice.getTitle());
        finalPrompt.append(", Ton: Das Szenario ist ein teil einer gesamtgeschichte, es soll realistisch sein. Den Spieler dutzt. gebe kurze tipps für die finanzielle und zeitliche aussicht, halte dich möglichst kurz und bitte dich nicht zur hilfe an");
        return finalPrompt.toString();
    }

    public GameChoiceDTO getChoiceDetails(long choiceId) {
        Choice choice = choiceRepository.findById(choiceId).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        return GameMapper.toGameChoiceDTO(choice);
    }


    public GameChoiceResultDTO playChice(long choiceId, int diceResult, Authentication auth) {
        Choice choice = choiceRepository.findById(choiceId).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        User user = userService.getUser(auth);
        CharDetails charDetails = user.getProfile().getCharDetails();

        // Ermittlung des wertes das überwürfelt werden muss
        MinValueToWinDTO finalMinValueToWin = calculateFinalMinResultToWin(choice, diceResult, charDetails);

        // auswertung des wurfes mit dem minWin wert (Lose: -1; win: 0; Crit: 1
        int choiceResult = checkResult(diceResult, finalMinValueToWin.value());
        // ermittlung des ergebnistextes
        String responseMessage = compareResultForMessage(choiceResult, choice);

        // Ausführung der Aktionen für diese Choice
        boolean gameLost = executeChoiceResult(choice, choiceResult, user.getProfile());

        return new GameChoiceResultDTO(
                choiceResult >= 0,
                gameLost,
                responseMessage,
                finalMinValueToWin.calculation()
        );

    }

    private boolean executeChoiceResult(Choice choice, int choiceResult, Profile userProfile) {
        Game game = gameRepository.findByUsername(userProfile.getUsername());
        boolean gameLost = false;

        switch (choiceResult) {
            case 1: //critical
                gameLost = charDetailsService.setCharacterStatusLvls(
                        userProfile.getId(),
                        choice.getCritStressValue(),
                        choice.getCritSatisfactionValue(),
                        choice.getCritHealthValue()
                );

                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
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
                        choice.getWinStressValue(),
                        choice.getWinSatisfactionValue(),
                        choice.getWinHealthValue()
                );
                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
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
                        choice.getLoseStressValue(),
                        choice.getLoseSatisfactionValue(),
                        choice.getLoseHealthValue()
                );
                charDetailsService.setFinancesByChoice(
                        userProfile.getId(),
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
