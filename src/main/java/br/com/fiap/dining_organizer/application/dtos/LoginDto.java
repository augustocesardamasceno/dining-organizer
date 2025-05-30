package br.com.fiap.dining_organizer.application.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank String login,
        @NotBlank String password
) {}
