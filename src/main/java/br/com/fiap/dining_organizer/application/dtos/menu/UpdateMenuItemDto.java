package br.com.fiap.dining_organizer.application.dtos.menu;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
public class UpdateMenuItemDto {
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private BigDecimal price;
    @Setter
    private Boolean dineInOnly;
    @Setter
    private String photoPath;
    @Setter
    @NotNull(message = "restaurantId é obrigatório para atualizar item de menu")
    private Long restaurantId;
    @Setter
    @NotNull(message = "ownerId é obrigatório")
    private Long ownerId;

}