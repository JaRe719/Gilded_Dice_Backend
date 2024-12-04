package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.mapper.CharMapper;
import de.jare.gildeddice.repositories.CharDetailsRepository;
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
}
