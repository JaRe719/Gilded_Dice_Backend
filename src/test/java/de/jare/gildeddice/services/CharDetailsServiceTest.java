package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.entities.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.CharDetailsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharDetailsServiceTest {

    @Mock
    private CharDetailsRepository charDetailsRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CharDetailsService charDetailsService;

    public CharDetailsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCharDetails_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetails charDetails = new CharDetails();
        charDetails.setIntelligence(10);
        charDetails.setNegotiate(8);

        Profile profileMock = new Profile();
        profileMock.setCharDetails(charDetails);

        when(userService.getUserProfile(auth)).thenReturn(profileMock);

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.getCharDetails(auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());

        verify(userService, times(1)).getUserProfile(auth);
    }

    @Test
    void testCreateOrUpdateCharDetails_NewCharacter_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetailsRequestDTO requestDTO = new CharDetailsRequestDTO(10, 8, 7, 6, 5, "avatar_url");

        Profile profileMock = new Profile();
        profileMock.setCharDetails(null); // Kein CharDetails vorhanden

        User userMock = new User();
        userMock.setProfile(profileMock);

        when(userService.getUser(auth)).thenReturn(userMock);

        // Simuliert das Speichern der neuen CharDetails
        when(charDetailsRepository.save(any(CharDetails.class))).thenAnswer(invocation -> {
            CharDetails savedCharDetails = invocation.getArgument(0);
            savedCharDetails.setId(1L); // ID simulieren
            return savedCharDetails;
        });

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.createOrUpdateCharDetails(requestDTO, auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());
        assertEquals(7, responseDTO.ability());
        assertEquals(6, responseDTO.planning());
        assertEquals(5, responseDTO.stamina());
        assertEquals("avatar_url", responseDTO.avatar());

        // Verifiziere die Interaktionen
        verify(userService, times(1)).getUser(auth);
        verify(charDetailsRepository, times(1)).save(any(CharDetails.class));
        verify(userService, times(1)).setUserCharToProfile(any(CharDetails.class), eq(auth));
    }



    @Test
    void testCreateOrUpdateCharDetails_UpdateCharacter_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetailsRequestDTO requestDTO = new CharDetailsRequestDTO(10, 8, 7, 6, 5, "avatar_url");

        CharDetails existingCharDetails = new CharDetails();
        existingCharDetails.setIntelligence(5);

        Profile profileMock = new Profile();
        profileMock.setCharDetails(existingCharDetails);

        User userMock = new User();
        userMock.setProfile(profileMock);

        when(userService.getUser(auth)).thenReturn(userMock);
        when(charDetailsRepository.save(existingCharDetails)).thenReturn(existingCharDetails);

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.createOrUpdateCharDetails(requestDTO, auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());

        verify(userService, times(1)).getUser(auth);
        verify(charDetailsRepository, times(1)).save(existingCharDetails);
        verify(userService, times(1)).setUserCharToProfile(existingCharDetails, auth);
    }

}