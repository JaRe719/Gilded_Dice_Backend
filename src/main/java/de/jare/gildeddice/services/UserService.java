package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.user.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.ProfileRepository;
import de.jare.gildeddice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User getUser(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    public Profile getUserProfile(Authentication auth) {
        User user = getUser(auth);
        return user.getProfile();
    }



    @Transactional
    public void newUserRegister(UserRegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        Profile profile = new Profile();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        profile.setUsername(dto.username());

        profile = profileRepository.save(profile);
        user.setProfile(profile);
        userRepository.save(user);
    }


    public void deleteUser(Authentication auth) {
        User existingUser = getUser(auth);
        userRepository.deleteById(existingUser.getId());
    }


    public void setUserCharToProfile(CharDetails charDetails, Authentication auth) {
        User user = getUser(auth);
        Profile profile = user.getProfile();
        profile.setCharDetails(charDetails);
        profileRepository.save(profile);
    }

    public void saveHighScore(Profile profile, int highScore) {
        if (highScore > profile.getHighScore()) {
            profile.setHighScore(highScore);
            profileRepository.save(profile);
        }
    }
}
