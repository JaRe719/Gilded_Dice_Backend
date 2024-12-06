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

    public GameService(GameRepository gameRepository, StoryRepository storyRepository, ChoiceRepository choiceRepository, NpcRepository npcRepository, AiService aiService, UserService userService) {
        this.gameRepository = gameRepository;
        this.storyRepository = storyRepository;
        this.choiceRepository = choiceRepository;
        this.npcRepository = npcRepository;
        this.aiService = aiService;
        this.userService = userService;
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
            choiceEntity.setLoseMessage(choice.loseMessage());
            choiceEntity.setCritMessage(choice.critMessage());
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
        Choice choice = choiceRepository.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        choice.setTitle(dto.title());
        choice.setSkill(Skill.valueOf(dto.skill()));
        choice.setMinDiceValue(dto.minDiceValue());
        choice.setStartMessage(dto.startMessage());
        choice.setWinMessage(dto.winMessage());
        choice.setLoseMessage(dto.loseMessage());
        choice.setCritMessage(dto.critMessage());
        choice.setNpc(npcRepository.findById(dto.npcId()).orElseThrow(() -> new EntityNotFoundException("npc not found!")));
        choiceRepository.save(choice);
    }
    //------


    public GamePhaseDTO getGamePhase(Authentication auth) {
        User user = userService.getUser(auth);
        Game game = gameRepository.findByUser(user);

        if (game == null) {
            game = new Game();
            game.setUser(user);
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





    public GameChoiceResultDTO calculateChoiceDiceResult(long choiceId, int diceResult, Authentication auth) {
        Choice choice = choiceRepository.findById(choiceId).orElseThrow(() -> new EntityNotFoundException("Choice not found!"));
        User user = userService.getUser(auth);
        CharDetails charDetails = user.getProfile().getCharDetails();

        int skillValue = switch (choice.getSkill()) {
            case INTELLIGENCE -> charDetails.getIntelligence();
            case NEGOTIATE -> charDetails.getNegotiate();
            case ABILITY -> charDetails.getAbility();
            case PLANNING -> charDetails.getPlanning();
            case STAMINA -> charDetails.getStamina();
        };

        int handicap = charDetails.getSimplification() + charDetails.getComplication();

        int finalMinResultToWin = choice.getMinDiceValue() - skillValue - handicap;
        String calcPathGoal = "eigentliche Gewinnschwelle: " + choice.getMinDiceValue()
                + " - " + choice.getSkill().getSkillname() + ": " + skillValue
                + " - Handicap: " + handicap + " = " + finalMinResultToWin
                + " Würfelwert: " + diceResult;
        System.out.println(calcPathGoal);

        String resultMessage;
        boolean choiceWin;

        if (diceResult == 20) {
            //kritischer erfolg
            resultMessage = choice.getCritMessage();
            choiceWin = true;
        } else if (diceResult > finalMinResultToWin) {
            //erfolgreiche Probe
            resultMessage = choice.getWinMessage();
            choiceWin = true;
        } else {
            //verloren
            resultMessage = choice.getLoseMessage();
            choiceWin = false;
        }

        return new GameChoiceResultDTO(
                choiceWin,
                false,
                resultMessage,
                calcPathGoal);
    }
}
