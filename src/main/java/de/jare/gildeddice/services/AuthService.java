package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.user.AuthResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public AuthResponseDTO getToken(Authentication auth) {
        if (auth == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        return new AuthResponseDTO(tokenService.generateToken(auth));
    }


}
