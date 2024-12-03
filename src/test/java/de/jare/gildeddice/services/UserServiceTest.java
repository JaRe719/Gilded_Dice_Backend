package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.ProfileRepository;
import de.jare.gildeddice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testNewUserRegister_Success() {
        // Arrange
        UserRegisterRequestDTO dto = new UserRegisterRequestDTO("test@example.com", "password", "testuser");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");

        Profile profileMock = new Profile();
        profileMock.setId(1L);
        profileMock.setUsername(dto.username());
        when(profileRepository.save(any(Profile.class))).thenReturn(profileMock);

        // Act
        assertDoesNotThrow(() -> userService.newUserRegister(dto));

        // Assert
        verify(userRepository, times(1)).findByEmail(dto.email());
        verify(passwordEncoder, times(1)).encode(dto.password());
        verify(profileRepository, times(1)).save(any(Profile.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testNewUserRegister_EmailAlreadyExists() {
        // Arrange
        UserRegisterRequestDTO dto = new UserRegisterRequestDTO("test@example.com", "password", "testuser");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.newUserRegister(dto));
        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(dto.email());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(profileRepository);
        verify(userRepository, times(0)).save(any(User.class));
    }

}