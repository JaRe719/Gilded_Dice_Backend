package de.jare.gildeddice.controller;

import de.jare.gildeddice.dtos.user.AuthResponseDTO;
import de.jare.gildeddice.dtos.user.UserRegisterRequestDTO;
import de.jare.gildeddice.services.AuthService;
import de.jare.gildeddice.services.CharDetailsService;
import de.jare.gildeddice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private UserService userService;
    private AuthService authService;
    private CharDetailsService charDetailsService;

    public AuthController(AuthService authService, CharDetailsService charDetailsService, UserService userService) {
        this.authService = authService;
        this.charDetailsService = charDetailsService;
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> newUserRegister(@RequestBody @Valid UserRegisterRequestDTO dto) {
        try {
            userService.newUserRegister(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(Authentication auth) {

        return new ResponseEntity<>(authService.getToken(auth), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Void> deleteUser(Authentication auth) {
        charDetailsService.delete(auth);
        userService.deleteUser(auth);
        return ResponseEntity.ok().build();
    }
}
