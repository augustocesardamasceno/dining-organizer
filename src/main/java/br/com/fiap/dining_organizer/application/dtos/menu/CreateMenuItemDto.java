package br.com.fiap.dining_organizer.application.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateMenuItemDto(
        @NotBlank String name,
        String description,
        @NotNull BigDecimal price,
        @NotNull Boolean dineInOnly,
        @NotBlank String photoPath,
        @NotNull Long restaurantId
) {}