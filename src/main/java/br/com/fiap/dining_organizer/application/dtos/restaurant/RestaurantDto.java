package br.com.fiap.dining_organizer.application.dtos.restaurant;

public record RestaurantDto(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId
) {}
