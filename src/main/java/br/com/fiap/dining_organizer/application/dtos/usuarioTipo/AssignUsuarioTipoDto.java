package br.com.fiap.dining_organizer.application.dtos.usuarioTipo;

import jakarta.validation.constraints.NotNull;

public record AssignUsuarioTipoDto(
        @NotNull Long usuarioTipoId
) {
}
