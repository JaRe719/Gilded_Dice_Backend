package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.user.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.users.character.CharDetails;
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


    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, ProfileRepository profileRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public User getUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Profile getUserProfile(Authentication auth) {
        User user = getUser(auth);
        return user.getProfile();
    }


    @Transactional
    public void registerNewUser(UserRegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = createUser(dto);
        Profile profile = createProfile(dto);

        profile = profileRepository.save(profile);
        user.setProfile(profile);
        userRepository.save(user);
    }

    private User createUser(UserRegisterRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        return user;
    }

    private Profile createProfile(UserRegisterRequestDTO dto) {
        Profile profile = new Profile();
        profile.setUsername(dto.username());
        return profile;
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
