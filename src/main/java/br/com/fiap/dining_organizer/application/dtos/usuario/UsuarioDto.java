package br.com.fiap.dining_organizer.application.dtos.usuario;

import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        @NotNull(message = "O nome do usuário não pode ser nulo")
        String name,
        @NotNull(message = "O email do usuário não pode ser nulo")
        String email,
        @NotNull(message = "O endereço do usuário não pode ser nulo")
        String adress,
        @NotBlank(message = "O login do usuário não pode ser vazio")
        String login,
        @NotBlank(message = "A senha do usuário não pode ser vazia")
        //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,
        UsuarioTipoCode usuarioTipoCode
) {}
