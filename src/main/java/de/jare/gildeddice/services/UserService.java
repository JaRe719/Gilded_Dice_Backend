package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void newUserRegister(UserRegisterRequestDTO dto) {
        User user = new User();
        Profile profile = new Profile();
        user.setEmail(dto.email());
    }
}
