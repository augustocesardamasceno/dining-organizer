package br.com.fiap.dining_organizer.application.dtos.menu;

import java.math.BigDecimal;

public record MenuItemDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean dineInOnly,
        String photoPath,
        Long restaurantId
) {}