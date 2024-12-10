package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.dtos.characters.MoneyResponseDTO;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.games.Game;
import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.entities.users.character.CharChoices;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.mapper.CharMapper;
import de.jare.gildeddice.repositories.CharDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CharDetailsService {

    private final PlusStoryService plusStoryService;
    private CharDetailsRepository charDetailsRepository;

    private UserService userService;

    public CharDetailsService(PlusStoryService plusStoryService, CharDetailsRepository charDetailsRepository, UserService userService) {
        this.plusStoryService = plusStoryService;
        this.charDetailsRepository = charDetailsRepository;
        this.userService = userService;
    }

    public CharDetailsResponseDTO getCharDetails(Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);

        return CharMapper.charToResponseDTO(userProfile.getCharDetails());
    }

    @Transactional
    public CharDetailsResponseDTO createOrUpdateCharDetails(CharDetailsRequestDTO dto, Authentication auth) {
        User user = userService.getUser(auth);
        Profile userProfile = user.getProfile();

        CharDetails charDetails = userProfile.getCharDetails();
        if (charDetails == null) {
            charDetails = new CharDetails();
            charDetails.setCharChoices(new CharChoices());
        }

        charDetails.setIntelligence(dto.intelligence());
        charDetails.setNegotiate(dto.negotiate());
        charDetails.setAbility(dto.ability());
        charDetails.setPlanning(dto.planning());
        charDetails.setStamina(dto.stamina());
        charDetails.setAvatar(dto.avatar());

        charDetails = charDetailsRepository.save(charDetails);
        userService.setUserCharToProfile(charDetails, auth);

        return CharMapper.charToResponseDTO(charDetails);
    }

    public String getUserAvatar(Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);
        return userProfile.getCharDetails().getAvatar();
    }

    public MoneyResponseDTO getAllFinancial(Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);
        return CharMapper.moneyToResponseDTO(userProfile.getCharDetails());
    }

    public void setFinancesByPhaseEnd(long charId, Game game) {
        int gamePhase = game.getPhase();
        CharDetails charDetails = charDetailsRepository.findById(charId).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));

        int totalMoney = charDetails.getMoney();

        if (gamePhase != 10 && gamePhase % 10 == 0) {
            if (charDetails.getInvest() > 0) {
                int skippedPhases = (10 - (game.getPlayedPhase() % 10)) % 10;
                totalMoney += ((charDetails.getIncome() + charDetails.getOutcome()) * (skippedPhases * 12));
                totalMoney += (int) ((charDetails.getInvest() * charDetails.getInvestmentPercent()) / 100.0f) * 120;
                totalMoney += charDetails.getInvest();
                charDetails.setInvest(0);
                charDetails.setInvestmentPercent(0);

            } else {
                charDetails.setInvestmentPercent(0);
                totalMoney += (charDetails.getIncome() + charDetails.getOutcome() * 12);
            }

            charDetails.setMoney(totalMoney);

            charDetailsRepository.save(charDetails);
        }

    }

    public void setFinancesByChoice(long charId, Integer incomeValue, Integer outcomeValue, Integer oneTimePayment) {
        CharDetails charDetails = charDetailsRepository.findById(charId).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));

        if (incomeValue != null) charDetails.setIncome(incomeValue);
        if (outcomeValue != null) charDetails.setOutcome(charDetails.getOutcome() + outcomeValue);
        if (oneTimePayment != null) charDetails.setMoney(charDetails.getMoney() + (oneTimePayment));

        charDetailsRepository.save(charDetails);
    }

    public void setInventoryByChoice(long id, Boolean study, Boolean scholarship, Boolean apprenticeship, Boolean job, Boolean property, Boolean rentApartment, Boolean car, Boolean driverLicense) {
        CharDetails charDetails = charDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));
        CharChoices charChoices = charDetails.getCharChoices();

        if (study != null) charChoices.setStudy(study);
        if (scholarship!= null) charChoices.setScholarship(scholarship);
        if (apprenticeship!= null) charChoices.setApprenticeship(apprenticeship);
        if (job!= null) charChoices.setJob(job);
        if (property!= null) charChoices.setProperty(property);
        if (rentApartment!= null) charChoices.setRentApartment(rentApartment);
        if (car!= null) charChoices.setCar(car);
        if (driverLicense!= null) charChoices.setDriverLicense(driverLicense);

        charDetailsRepository.save(charDetails);
    }

    public boolean setCharacterStatusLvls(long id, int gamePhase, Integer stressValue, Integer satisfactionValue, Integer healthValue) {
        CharDetails charDetails = charDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));

        int handicap = 0;
        int stresslvl = 0;
        int satisfactionlvl = 0;
        int healthlvl = 0;
        boolean gameEnd = false;

        if (stressValue != null) {
            stresslvl += stressValue;
        }
        charDetails.adjustStressLvl(stresslvl);

        //-- Stress
        if (charDetails.getStressLvl() == 10) gameEnd = true;
        else if (charDetails.getStressLvl() >= 8 && charDetails.getStressLvl() < 10) {
            handicap -= 2;
            if (gamePhase % 10 == 0) healthlvl -= 1;
        }
        else if (charDetails.getStressLvl() >= 5 && charDetails.getStressLvl() < 8) handicap -= 1;


        //-- Satisfaction
        if (satisfactionValue != null) {
            satisfactionlvl +=  satisfactionValue;
        }
        charDetails.adjustSatisfactionLvl(satisfactionlvl);

        if (charDetails.getSatisfactionLvl() <= 2) {
            handicap -= 2;
            if (gamePhase % 10 == 0) healthlvl -= 1;
        }
        else if (charDetails.getSatisfactionLvl() == 3) handicap -= 1;
        else if (charDetails.getSatisfactionLvl() > 5) handicap += 1;


        //-- health
        if (healthValue != null) {
            healthlvl += healthValue;
        }
        charDetails.adjustHealthLvl(healthlvl);

        if (charDetails.getHealthLvl() == 0) gameEnd = true;


        //-----
        charDetails.setHandicap(handicap);
        charDetailsRepository.save(charDetails);
        return gameEnd;
    }

    public void resetChar(Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);
        CharDetails charDetails = userProfile.getCharDetails();
        System.out.print(charDetails);
        charDetails.setStressLvl(0);
        charDetails.setSatisfactionLvl(5);
        charDetails.setHealthLvl(20);
        charDetails.setHandicap(0);
        charDetails.setIncome(0);
        charDetails.setOutcome(0);
        charDetails.setInvest(0);
        charDetails.setInvestmentPercent(0);
        charDetails.setMoney(0);

        charDetails.setCharChoices(new CharChoices());
        charDetailsRepository.save(charDetails);
        System.out.print(charDetails);
    }

    public void setInvesting(long plusStoryId, Integer incomeValue, Authentication auth) {
        PlusStory plusStory = plusStoryService.getPlusStory(plusStoryId);
        Profile userProfile = userService.getUserProfile(auth);
        CharDetails charDetails = userProfile.getCharDetails();

        if (!plusStory.getCategory().equals(Category.INVESTMENT)) throw new WrongThreadException("PlusStory is not an Inventory Story");
        if (incomeValue >= 0) {
            charDetails.setInvest(incomeValue);
        } else throw new IllegalArgumentException("Investment value negative or null");

        charDetailsRepository.save(charDetails);
    }
}
