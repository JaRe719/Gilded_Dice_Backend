package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.AuthResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public AuthResponseDTO getToken(Authentication auth) {
        return new AuthResponseDTO(tokenService.generateToken(auth));
    }


}
