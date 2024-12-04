package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.entities.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.mapper.CharMapper;
import de.jare.gildeddice.repositories.CharDetailsRepository;
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
}
