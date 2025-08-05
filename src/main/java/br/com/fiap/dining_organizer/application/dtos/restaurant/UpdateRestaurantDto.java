package br.com.fiap.dining_organizer.application.dtos.restaurant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateRestaurantDto {

    // getters e setters
    @Setter
    private String name;
    @Setter
    private String address;
    @Setter
    private String cuisineType;
    @Setter
    private String openingHours;
    @NotNull(message = "ownerId é obrigatório para atualizar o restaurante")
    @Setter
    private Long ownerId;

}
