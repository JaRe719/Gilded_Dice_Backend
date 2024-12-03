package de.jare.gildeddice.dtos;

public record UserRegisterRequestDTO(

        String email,
        String password,
        String username
) {
}
