package de.jare.gildeddice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDTO(

        @Email
        String email,
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
