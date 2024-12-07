package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.user.AuthResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetToken_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(tokenService.generateToken(auth)).thenReturn("generatedToken");

        // Act
        AuthResponseDTO response = authService.getToken(auth);

        // Assert
        assertNotNull(response);
        assertEquals("generatedToken", response.token());
    }

    @Test
    void testGetToken_NullAuth() {
        // Arrange
        Authentication auth = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.getToken(auth));
        assertEquals("Authentication object cannot be null", exception.getMessage());
    }

}