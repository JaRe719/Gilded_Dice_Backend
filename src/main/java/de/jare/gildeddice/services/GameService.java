package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.games.*;
import de.jare.gildeddice.entities.Npc;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.enums.Skill;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.games.storys.Choice;
import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.entities.users.User;
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

    public void createNpc(String npcName) {
        Npc npc = new Npc();
        npc.setName(npcName);
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
            gameRepository.save(game);
            game.setPhase(1);
        }

        Story story = storyRepository.findByPhase(game.getPhase());
        System.out.println(aiService.callApi(story, user));


        return null;
    }
}
