package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.user.UserRegisterRequestDTO;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.ProfileRepository;
import de.jare.gildeddice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

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


    @Test
    void testDeleteUser_Success() {
        // Arrange
        String email = "test@example.com";
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);

        User userMock = new User();
        userMock.setId(1L);
        userMock.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));

        // Act
        assertDoesNotThrow(() -> userService.deleteUser(auth));

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).deleteById(userMock.getId());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(auth));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(0)).deleteById(anyLong());
    }


    @Test
    void testSetUserCharToProfile_Success() {
        // Arrange
        String email = "test@example.com";
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);

        CharDetails charDetails = new CharDetails();

        Profile profileMock = new Profile();
        profileMock.setId(1L);

        User userMock = new User();
        userMock.setEmail(email);
        userMock.setProfile(profileMock);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userMock));
        when(profileRepository.save(any(Profile.class))).thenReturn(profileMock);

        // Act
        assertDoesNotThrow(() -> userService.setUserCharToProfile(charDetails, auth));

        // Assert
        assertEquals(charDetails, profileMock.getCharDetails());
        verify(userRepository, times(1)).findByEmail(email);
        verify(profileRepository, times(1)).save(profileMock);
    }

    @Test
    void testSetUserCharToProfile_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        CharDetails charDetails = new CharDetails();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.setUserCharToProfile(charDetails, auth));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verifyNoInteractions(profileRepository);
    }

    @Test
    void testSaveHighScore_Success() {
        // Arrange
        String username = "Player1";

        Profile profileMock = new Profile();
        profileMock.setId(1L);
        profileMock.setUsername(username);

        when(profileRepository.save(any(Profile.class))).thenReturn(profileMock);

        // Act
        assertDoesNotThrow(() -> userService.saveHighScore(profileMock, 1000));

        // Assert
        verify(profileRepository, times(1)).save(profileMock);
    }

    @Test
    void testSaveHighScore_newHighscoreIsLowerAsInProfile() {
        // Arrange
        String username = "Player1";

        Profile profileMock = new Profile();
        profileMock.setId(1L);
        profileMock.setUsername(username);
        profileMock.setHighScore(99999999);

        when(profileRepository.save(any(Profile.class))).thenReturn(profileMock);

        // Act
        assertDoesNotThrow(() -> userService.saveHighScore(profileMock, 1000));

        // Assert
        verify(profileRepository, times(0)).save(profileMock);
    }

}