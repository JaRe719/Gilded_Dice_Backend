package de.jare.gildeddice.mapper;

import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.dtos.characters.MoneyResponseDTO;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.services.CharDetailsService;

public class CharMapper {


    public static CharDetailsResponseDTO charToResponseDTO(Profile userProfile) {
        CharDetails charDetails = userProfile.getCharDetails();
        return new CharDetailsResponseDTO(charDetails.getId(),
                userProfile.getUsername(),
                charDetails.getStressLvl(),
                charDetails.getSatisfactionLvl(),
                charDetails.getHealthLvl(),
                charDetails.getIntelligence(),
                charDetails.getNegotiate(),
                charDetails.getAbility(),
                charDetails.getPlanning(),
                charDetails.getStamina(),
                charDetails.getHandicap(),
                charDetails.getCharChoices().isStudy(),
                charDetails.getCharChoices().isScholarship(),
                charDetails.getCharChoices().isApprenticeship(),
                charDetails.getCharChoices().isJob(),
                charDetails.getCharChoices().isProperty(),
                charDetails.getCharChoices().isRentApartment(),
                charDetails.getCharChoices().isCar(),
                charDetails.getCharChoices().isDriverLicense(),
                charDetails.getAvatar());
    }

    public static MoneyResponseDTO moneyToResponseDTO(CharDetails charDetails) {
        return new MoneyResponseDTO(charDetails.getIncome(),
                charDetails.getOutcome(),
                charDetails.getInvest(),
                charDetails.getMoney());
    }
}
