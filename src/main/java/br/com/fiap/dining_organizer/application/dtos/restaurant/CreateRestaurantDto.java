package br.com.fiap.dining_organizer.application.dtos.restaurant;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRestaurantDto(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String cuisineType,
        @NotBlank String openingHours,
        @NotNull Long ownerId
) {}
