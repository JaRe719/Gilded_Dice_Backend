package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.dtos.characters.MoneyResponseDTO;
import de.jare.gildeddice.entities.character.CharChoices;
import de.jare.gildeddice.entities.character.CharDetails;
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

    private CharDetailsRepository charDetailsRepository;

    private UserService userService;

    public CharDetailsService(CharDetailsRepository charDetailsRepository, UserService userService) {
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

    public String setUserAvatar(String avatarUrl, Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);
        CharDetails charDetails = userProfile.getCharDetails();
        charDetails.setAvatar(avatarUrl);
        charDetails = charDetailsRepository.save(charDetails);
        return charDetails.getAvatar();
    }

    public MoneyResponseDTO getAllFinancial(Authentication auth) {
        Profile userProfile = userService.getUserProfile(auth);
        return CharMapper.moneyToResponseDTO(userProfile.getCharDetails());
    }

    public void setFinancesByChoice(long id, Integer incomeValue, Integer outcomeValue, Integer oneTimePayment){
        CharDetails charDetails = charDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));


        if (incomeValue != null) charDetails.setIncome(incomeValue);
        if (outcomeValue != null) charDetails.setOutcome(charDetails.getOutcome() + outcomeValue);
        if (oneTimePayment != null) charDetails.setMoney(charDetails.getMoney() + (oneTimePayment));

        charDetailsRepository.save(charDetails);
        System.out.println(oneTimePayment);
        System.out.println(charDetails.getMoney());
    }

    public void setInventoryByChoice(long id, Boolean study, Boolean scholarship, Boolean apprenticeship, Boolean job, Boolean property, Boolean rentApartment, Boolean car) {
        CharDetails charDetails = charDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));
        CharChoices charChoices = charDetails.getCharChoices();

        if (study != null) charChoices.setStudy(study);
        if (scholarship!= null) charChoices.setScholarship(scholarship);
        if (apprenticeship!= null) charChoices.setApprenticeship(apprenticeship);
        if (job!= null) charChoices.setJob(job);
        if (property!= null) charChoices.setProperty(property);
        if (rentApartment!= null) charChoices.setRentApartment(rentApartment);
        if (car!= null) charChoices.setCar(car);

        charDetailsRepository.save(charDetails);
    }

    public boolean setCharacterStatusLvls(long id, Integer stressValue, Integer satisfactionValue, Integer healthValue) {
        CharDetails charDetails = charDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CharDetails not found!"));

        int handicap = 0;
        int charStresslvl = charDetails.getStressLvl();
        int charSatisfactionlvl = charDetails.getSatisfactionLvl();
        int charHealthlvl = charDetails.getHealthLvl();
        boolean gameEnd = false;

        if (stressValue != null) {
            charStresslvl = charStresslvl + stressValue;
        }
        charDetails.setStressLvl(charStresslvl);

        //-- Stress
        if (charStresslvl == 10) gameEnd = true;
        else if (charStresslvl >= 8 && charStresslvl < 10) {
            handicap -= 2;
            charHealthlvl -= 1;
        }
        else if (charStresslvl >= 5 && charStresslvl < 8) handicap -= 1;


        //-- Satisfaction
        if (satisfactionValue != null) {
            charSatisfactionlvl =  charSatisfactionlvl + satisfactionValue;
        }
        charDetails.setSatisfactionLvl(charSatisfactionlvl);

        if (charSatisfactionlvl <= 2) {
            handicap -= 2;
            charHealthlvl -= 1;
        }
        else if (charSatisfactionlvl == 3) handicap -= 1;
        else if (charSatisfactionlvl > 5) handicap += 1;


        //-- health
        if (healthValue != null) {
           charHealthlvl = charHealthlvl + healthValue;
        }
        charDetails.setHealthLvl(charHealthlvl);

        if (charDetails.getHealthLvl() == 0) gameEnd = true;


        //-----
        charDetails.setHandicap(handicap);
        charDetailsRepository.save(charDetails);
        return gameEnd;
    }
}
