package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.ProfileRepository;
import de.jare.gildeddice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        User existingUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found:"));
        userRepository.deleteById(existingUser.getId());
    }
}
