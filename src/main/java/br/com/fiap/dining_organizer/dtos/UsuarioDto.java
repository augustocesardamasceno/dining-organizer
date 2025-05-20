package br.com.fiap.dining_organizer.dtos;

import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        @NotNull(message = "O nome do usuário não pode ser nulo")
        String name,
        @NotNull(message = "O email do usuário não pode ser nulo")
        String email,
        @NotNull(message = "O endereço do usuário não pode ser nulo")
        String adress
) {
}
