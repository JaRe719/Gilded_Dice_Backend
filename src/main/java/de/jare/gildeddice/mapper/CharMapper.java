package de.jare.gildeddice.mapper;

import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.dtos.characters.MoneyResponseDTO;
import de.jare.gildeddice.entities.character.CharDetails;

public class CharMapper {


    public static CharDetailsResponseDTO charToResponseDTO(CharDetails charDetails) {
        return new CharDetailsResponseDTO(charDetails.getId(),
                charDetails.getStressLvl(),
                charDetails.getSatisfactionLvl(),
                charDetails.getHealthLvl(),
                charDetails.getIntelligence(),
                charDetails.getNegotiate(),
                charDetails.getAbility(),
                charDetails.getPlanning(),
                charDetails.getStamina(),
                charDetails.isProperty(),
                charDetails.isRentApartment(),
                charDetails.isCar(),
                charDetails.getAvatar());
    }

    public static MoneyResponseDTO moneyToResponseDTO(CharDetails charDetails) {
        return new MoneyResponseDTO(charDetails.getIncome(),
                charDetails.getOutcome(),
                charDetails.getInvest(),
                charDetails.getMoney());
    }
}