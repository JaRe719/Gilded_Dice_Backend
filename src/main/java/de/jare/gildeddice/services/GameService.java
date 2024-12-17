package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.ai.response.KSuitAiResponseDTO;
import de.jare.gildeddice.dtos.games.choice.ChoiceCreateDTO;
import de.jare.gildeddice.dtos.games.choice.ChoiceUpdateDTO;
import de.jare.gildeddice.dtos.games.choice.GameChoiceDTO;
import de.jare.gildeddice.dtos.games.choice.GameChoiceResultDTO;
import de.jare.gildeddice.dtos.games.game.*;
import de.jare.gildeddice.dtos.games.plusstorys.PlusStoryCreateDTO;
import de.jare.gildeddice.dtos.games.plusstorys.PlusStoryUpdateDTO;
import de.jare.gildeddice.dtos.games.story.StoryCreateDTO;
import de.jare.gildeddice.dtos.games.story.StoryUpdateDTO;
import de.jare.gildeddice.entities.games.storys.*;
import de.jare.gildeddice.entities.users.character.CharChoices;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.enums.Skill;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.games.choices.Choice;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.mapper.GameMapper;
import de.jare.gildeddice.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final ProfileRepository profileRepository;
    private final CharDetailsRepository charDetailsRepository;
    private GameRepository gameRepository;
    private StoryRepository storyRepository;
    private ChoiceRepository choiceRepository;
    private NpcRepository npcRepository;
    private PlusStoryRepository plusStoryRepository;

    private AiService aiService;
    private UserService userService;
    private CharDetailsService charDetailsService;

    private PlusStoryService plusStoryService;

    private HighScoreService highScoreService;

    public GameService(AiService aiService, CharDetailsService charDetailsService, ChoiceRepository choiceRepository, GameRepository gameRepository, HighScoreService highScoreService, NpcRepository npcRepository, PlusStoryRepository plusStoryRepository, PlusStoryService plusStoryService, StoryRepository storyRepository, UserService userService, ProfileRepository profileRepository, CharDetailsRepository charDetailsRepository) {
        this.aiService = aiService;
        this.charDetailsService = charDetailsService;
        this.choiceRepository = choiceRepository;
        this.gameRepository = gameRepository;
        this.highScoreService = highScoreService;
        this.npcRepository = npcRepository;
        this.plusStoryRepository = plusStoryRepository;
        this.plusStoryService = plusStoryService;
        this.storyRepository = storyRepository;
        this.userService = userService;
        this.profileRepository = profileRepository;
        this.charDetailsRepository = charDetailsRepository;
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
        story.setSkippable(dto.skippable());
        story.setPhaseEnd(dto.phaseEnd());
        story.setGameEnd(dto.gameEnd());
        story.setChoices(createChoiceList(dto.choices()));
        storyRepository.save(story);
    }

    private List<Choice> createChoiceList(List<ChoiceCreateDTO> choices) {
        List<Choice> choiceEntities = new ArrayList<>();
        for (ChoiceCreateDTO choice : choices) {
            Choice choiceEntity = new Choice();
            choiceEntity.setTitle(choice.title());
            choiceEntity.setSkill(Skill.valueOf(choice.skill()));
            choiceEntity.setMinDiceValue(choice.minDiceValue());
            choiceEntity.setCost(choice.cost());
            choiceEntity.setReturning(choice.returning());
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
            choiceEntity.setWinDriverLicense(choice.winDriverLicense());

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
            choiceEntity.setLoseDriverLicense(choice.loseDriverLicense());

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
        story.setCategory(Category.valueOf(dto.category()));
        story.setTitle(dto.title());
        story.setPhase(dto.phase());
        story.setSkippable(dto.skippable());
        story.setPhaseEnd(dto.phaseEnd());
        story.setPrompt(dto.prompt());
        story.setGameEnd(dto.gameEnd());
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
        choiceEntity.setCost(dto.cost());
        choiceEntity.setReturning(dto.returning());
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
        choiceEntity.setWinDriverLicense(dto.winDriverLicense());

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
        choiceEntity.setLoseDriverLicense(dto.loseDriverLicense());

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



    public GamePhaseDTO getGamePhase(Authentication auth) {
        User user = userService.getUser(auth);
        if (user.getProfile().getCharDetails() == null) throw new IllegalStateException("no Char");

        Game game = getGame(user);

        if (game.getCurrentGamePhase() != null) {
            return game.getCurrentGamePhase();
        } else charDetailsService.setFinancesByPhaseEnd(user.getProfile().getCharDetails().getId(), game);

        if (game.isGameEnd() || game.isGameLost()) {
            GamePhaseDTO summary = getGameSummary(game);
            game.setCurrentGamePhase(summary);
            gameRepository.save(game);
            return summary;
        }
        int activeGamePhase = game.getPhase();

        game.setAvailablePlusStories(addNewAvailablePlusStories(user, game));
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 10);

        if (!game.isPlusStoryRunLastRound() && (game.getPhase() == 12 || (game.getPhase() > 12 && (game.getPhase() % 2 == 0 && (randomIndex >= 0 && randomIndex < 5))))) {
            game.setPlusStoryRunLastRound(true);
            try {
                return startRandomPlusStory(game, user);
            } catch (EmptyStackException ignored) {
                System.out.println("LOG: empty PlusStory stack");
            }
        } else if (game.getPhase() % 2 == 1) {
            game.setPlusStoryRunLastRound(false);
        }

        Story story = storyRepository.findByPhase(game.getPhase());
        if (story == null) {
            gameRepository.save(game);
            return new GamePhaseDTO("null", "error", "Story not found for phase " + game.getPhase(), true, true, new ArrayList<>());
        }

        String finalPrompt = createCompletedPrompt(story.getPrompt(), story.getChoices(), story.getPhase(), user);
        //KSuitAiResponseDTO responseDTO = aiService.callApi(finalPrompt);

        setNextGamePhase(story, game);
        saveHighScoreWhenGameIsEnd(user.getProfile(), game, story.isGameEnd());

        GamePhaseDTO gamePhaseDTO = GameMapper.toGamePhaseDTO(story.getCategory(), story.getTitle(), "Test " +activeGamePhase + " " + finalPrompt , story.isSkippable(), game.isGameEnd(), story.getChoices()); //GameMapper.toGamePhaseDTO(story.getCategory(),story.getTitle(), responseDTO.choices().getFirst().message().content(), story.isSkippable(), game.isGameEnd(), story.getChoices());
        game.setCurrentGamePhase(gamePhaseDTO);
        gameRepository.save(game);

        return gamePhaseDTO;
    }

    private GamePhaseDTO getGameSummary(Game game) {
        Profile profile = profileRepository.findByUsername(game.getUsername());
        String finalPrompt = generateFinalPrompt(profile, game);
        KSuitAiResponseDTO responseDTO = aiService.callApi(finalPrompt);

        return GameMapper.toGamePhaseDTO(Category.FATE, "GAMEEND", responseDTO.choices().getFirst().message().content(), false, game.isGameEnd(), new ArrayList<>());
    }

    private String generateFinalPrompt(Profile profile, Game game) {
        String username = profile.getUsername();
        CharDetails charDetails = profile.getCharDetails();
        CharChoices charChoices = charDetails.getCharChoices();

        String finalPrompt = "Erstelle eine kurze grobe zusammenfassung des PnP-Spielcharakter in deutscher sprache. basierend auf den folgenden informationen: " + "charaktername: " + username +
                ", Finanzielle lage: Monatliches Einkommen: " + charDetails.getIncome() + ", Monatliche Ausgaben: " + charDetails.getOutcome() + " insgesamtes Vermögen: " + charDetails.getMoney() +
                ", es wird  folgendes erreicht: Studium=" + charChoices.isStudy() + " mit Stipendium=" + charChoices.isScholarship() +
                ", Berufliche ausbildung= " + charChoices.isApprenticeship() +
                ", Beruf= " + charChoices.isJob() +
                ", eigenes Haus=" + charChoices.isProperty() + " Wohnt zur miete=" + charChoices.isRentApartment() +
                ", Eigenes Fahrzeug: " + charChoices.isCar() + " Führerschein=" + charChoices.isDriverLicense() +
                ", allgemeiner gesundheitszustand: Gesundheit=" + charDetails.getHealthLvl() + "zufriedenheit=" + charDetails.getSatisfactionLvl() + " (skala schlecht 0 bis 10 gut)" +
                ", Stressniveau: " + charDetails.getStressLvl() + " (skala gut 0 bis 10 schlecht)" +
                ", Ton: gebe kurze tipps für die finanzielle und zeitliche aussicht, halte dich möglichst kurz und bitte dich nicht zur hilfe an";
        return finalPrompt;
    }


    private GamePhaseDTO startRandomPlusStory(Game game, User user) {
        List<PlusStory> plusStories = game.getAvailablePlusStories();
        if (plusStories.isEmpty()) throw new EmptyStackException();

        int randomIndex = ThreadLocalRandom.current().nextInt(0, plusStories.size());
        PlusStory randomPlusStory = plusStories.get(randomIndex);

        if (randomPlusStory.isOneTime()) {
            game.getUsedPlusStories().add(randomPlusStory.getId());
            plusStories.remove(randomPlusStory);
        } else game.getAvailablePlusStories().remove(randomPlusStory);

        String finalPrompt = createCompletedPrompt(randomPlusStory.getPrompt(), randomPlusStory.getChoices(), randomPlusStory.getPhase(), user);
//        KSuitAiResponseDTO responseDTO = aiService.callApi(finalPrompt);
//        saveHighScoreWhenGameIsEnd(user.getProfile(), game, false);

        GamePhaseDTO gamePhaseDTO = GameMapper.toGamePhaseDTO(randomPlusStory.getCategory(), randomPlusStory.getTitle(), "Test Plus " + game.getPhase() + " " + finalPrompt, randomPlusStory.isSkippable(), false, randomPlusStory.getChoices());
        // return GameMapper.toGamePhaseDTO(randomPlusStory.getCategory(), responseDTO.choices().getFirst().message().content(), randomPlusStory.isSkippable(), false, randomPlusStory.getChoices());

        game.setCurrentGamePhase(gamePhaseDTO);
        gameRepository.save(game);
        return gamePhaseDTO;
    }

    private String createCompletedPrompt(String storyPrompt, List<Choice> choices, int storyPhase , User user) {
        String username = user.getProfile().getUsername();
        CharDetails charDetails = user.getProfile().getCharDetails();

        StringBuilder finalPrompt = new StringBuilder("Erstelle ein kurzes (2-3 sätze max) und in deutsch verfasstes, individuellen text für folgendes PnP-Szenarion basierend auf den folgenden informationen: ");
        finalPrompt.append("charaktername: ").append(username);
        finalPrompt.append(", Szenario: ").append(storyPrompt);
        finalPrompt.append(", Endscheidung: ");
        for (Choice choice : choices) finalPrompt.append(choice.getTitle());
        finalPrompt.append(", Ton: Das Szenario ist ein teil einer gesamtgeschichte, es soll realistisch sein. Den Spieler dutzt. gebe kurze tipps für die finanzielle und zeitliche aussicht, halte dich möglichst kurz und bitte dich nicht zur hilfe an");
        if (storyPhase != 10) finalPrompt.append("ladde die Begrüßung weg und steig gleich in das Szenario ein");
        return finalPrompt.toString();
    }

    private void setNextGamePhase(Story story, Game game) {
        if (story.isPhaseEnd()) game.setPhase(((game.getPhase() + 9) / 10) * 10);
        else if (story.isGameEnd()) game.setGameEnd(true);
        else game.setPhase(game.getPhase() + 1);
    }

    private Game getGame(User user) {
        Optional<Game> existingGame = gameRepository.findByUsername(user.getProfile().getUsername());
        Game game = new Game();
        if (existingGame.isPresent()) {
            game = existingGame.get();
            return game;
        } else {
            game.setUsername(user.getProfile().getUsername());
            game.setPhase(10);
            return game;
        }
    }

    private void saveHighScoreWhenGameIsEnd(Profile profile, Game game, boolean gameEnd) {
        if (game.isGameLost() || gameEnd) {
            int highScore = profile.getCharDetails().getMoney();
            if (profile.getHighScore() < highScore) {
                userService.saveHighScore(profile, highScore);
                highScoreService.saveHighScore(profile);
            }
        }
    }

    private List<PlusStory> addNewAvailablePlusStories(User user, Game game) {
        CharDetails userCharacter = user.getProfile().getCharDetails();
        List<PlusStory> availablePlusStories = game.getAvailablePlusStories();
        Set<Long> usedPlusStories = game.getUsedPlusStories();

        List<PlusStory> allPlusStory = plusStoryService.getAllPlusStory();
        return allPlusStory.stream()
                .filter(ps -> !availablePlusStories.contains(ps.getId()))
                .filter(ps -> !usedPlusStories.contains(ps.getId()))
                .filter(ps -> meetsRequirements(ps.getRequirement(), userCharacter))
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean meetsRequirements(Requirement requirement, CharDetails userCharacter) {
        CharChoices userChoices = userCharacter.getCharChoices();

        if (requirement == null) {
            return true;
        }

        if (requirement.getHasStudie() != null && requirement.getHasStudie() != userChoices.isStudy()) return false;
        if (requirement.getHasScholarship() != null && requirement.getHasScholarship() != userChoices.isScholarship()) return false;
        if (requirement.getHasApprenticeship() != null && requirement.getHasApprenticeship() != userChoices.isApprenticeship()) return false;
        if (requirement.getHasSecondJob() != null && requirement.getHasSecondJob() != userChoices.isSecondJob()) return false;
        if (requirement.getHasJob() != null && requirement.getHasJob() != userChoices.isJob()) return false;

        if (requirement.getInsurance() != null && requirement.getInsurance() != userChoices.isInsurance()) return false;

        if (requirement.getHasHomeByParents() != null && requirement.getHasHomeByParents() != userChoices.isHomeByParents()) return false;
        if (requirement.getHasSharedApartment() != null && requirement.getHasSharedApartment() != userChoices.isSharedApartment()) return false;
        if (requirement.getHasRentedApartment() != null && requirement.getHasRentedApartment() != userChoices.isRentApartment()) return false;

        if (requirement.getHasProperty() != null && requirement.getHasProperty() != userChoices.isProperty()) return false;
        if (requirement.getHasCar() != null && requirement.getHasCar() != userChoices.isCar()) return false;
        if (requirement.getHasDriverLicense() != null && requirement.getHasDriverLicense() != userChoices.isDriverLicense()) return false;

        if (requirement.getHasInvested() != null && userCharacter.getInvest() <= 0) return false;
        if (requirement.getStressStatusLvl() != null && userCharacter.getStressLvl() > requirement.getStressStatusLvl()) return false;
        if (requirement.getSatisfactionStatusLvl() != null && userCharacter.getSatisfactionLvl() < requirement.getSatisfactionStatusLvl()) return false;
        if (requirement.getHealthStatusLvl() != null && userCharacter.getHealthLvl() < requirement.getHealthStatusLvl()) return false;

        return true;
    }


    public void resetGame(Authentication auth) {
        User user = userService.getUser(auth);
        Game game = gameRepository.findByUsername(user.getProfile().getUsername()).orElseThrow(() -> new EntityNotFoundException("GameNotFound"));

        game.setPhase(10);
        game.setGameLost(false);
        game.setGameEnd(false);
        game.getAvailablePlusStories().clear();
        game.getUsedPlusStories().clear();

        game.setCurrentGamePhase(null);
        gameRepository.save(game);

        charDetailsService.resetChar(auth);
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

        game.setCurrentGamePhase(null);
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
                        choice.getWinCar(),
                        choice.getWinDriverLicense()
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
                        choice.getWinCar(),
                        choice.getWinDriverLicense()
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
                        choice.getLoseCar(),
                        choice.getWinDriverLicense()
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

    public void createPlusStory(PlusStoryCreateDTO dto) {
        PlusStory plusStory = new PlusStory();
        plusStory.setCategory(Category.valueOf(dto.category()));
        plusStory.setTitle(dto.title());
        plusStory.setPrompt(dto.prompt());
        plusStory.setSkippable(dto.skippable());
        plusStory.setOneTime(dto.oneTime());

        Requirement requirement = new Requirement();
        requirement.setHasStudie(dto.requirement().hasStudie());
        requirement.setHasScholarship(dto.requirement().hasScholarship());
        requirement.setHasApprenticeship(dto.requirement().hasApprenticeship());
        requirement.setHasSecondJob(dto.requirement().hasSecondJob());
        requirement.setHasJob(dto.requirement().hasJob());

        requirement.setInsurance(dto.requirement().insurance());

        requirement.setHasHomeByParents(dto.requirement().hasHomeByParents());
        requirement.setHasSharedApartment(dto.requirement().hasSharedApartment());
        requirement.setHasRentedApartment(dto.requirement().hasRentedApartment());

        requirement.setHasProperty(dto.requirement().hasProperty());
        requirement.setHasCar(dto.requirement().hasCar());
        requirement.setHasDriverLicense(dto.requirement().hasDriverLicense());

        requirement.setHasInvested(dto.requirement().hasInvested());
        requirement.setStressStatusLvl(dto.requirement().satisfactionStatusLvl());
        requirement.setHealthStatusLvl(dto.requirement().healthStatusLvl());
        plusStory.setRequirement(requirement);
        plusStory.setChoices(createChoiceList(dto.choices()));

        plusStoryRepository.save(plusStory);
    }

    public void updatePlusStory(PlusStoryUpdateDTO dto) {
        PlusStory plusStory = plusStoryRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Story not found!"));
        plusStory.setCategory(Category.valueOf(dto.category()));
        plusStory.setTitle(dto.title());
        plusStory.setPrompt(dto.prompt());
        plusStory.setSkippable(dto.skippable());
        plusStory.setOneTime(dto.oneTime());

        Requirement requirement = plusStory.getRequirement();
        requirement.setHasStudie(dto.requirement().hasStudie());
        requirement.setHasScholarship(dto.requirement().hasScholarship());
        requirement.setHasApprenticeship(dto.requirement().hasApprenticeship());
        requirement.setHasSecondJob(dto.requirement().hasSecondJob());
        requirement.setHasJob(dto.requirement().hasJob());

        requirement.setInsurance(dto.requirement().insurance());

        requirement.setHasHomeByParents(dto.requirement().hasHomeByParents());
        requirement.setHasSharedApartment(dto.requirement().hasSharedApartment());
        requirement.setHasRentedApartment(dto.requirement().hasRentedApartment());

        requirement.setHasProperty(dto.requirement().hasProperty());
        requirement.setHasCar(dto.requirement().hasCar());
        requirement.setHasDriverLicense(dto.requirement().hasDriverLicense());

        requirement.setHasInvested(dto.requirement().hasInvested());
        requirement.setStressStatusLvl(dto.requirement().satisfactionStatusLvl());
        requirement.setHealthStatusLvl(dto.requirement().healthStatusLvl());
        plusStory.setRequirement(requirement);

        plusStoryRepository.save(plusStory);
    }

    public void createNpcFromList(List<NpcCreateListDTO> dto) {
        for (NpcCreateListDTO newNpc : dto) {
            createNpc(newNpc.npcName(), newNpc.filename());
        }
    }

    public void skipGame(Authentication auth) {
        Profile profile = userService.getUserProfile(auth);
        Game game = gameRepository.findByUsername(profile.getUsername()).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        game.setCurrentGamePhase(null);
        gameRepository.save(game);

    }
}

