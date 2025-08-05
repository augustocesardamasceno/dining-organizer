package br.com.fiap.dining_organizer.application.dtos.usuarioTipo;

import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import jakarta.validation.constraints.NotBlank;

public record UsuarioTipoDto(
        Long id,
        UsuarioTipoCode code,
        @NotBlank(message = "A descrição do usuário não pode estar vazia") String description
) {}
