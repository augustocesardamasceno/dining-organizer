package br.com.fiap.dining_organizer.application.dtos.restaurant;

import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {

    public RestaurantDto toDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getOwner().getId()
        );
    }

    public Restaurant toEntity(CreateRestaurantDto dto, Usuario owner) {
        return Restaurant.builder()
                .name(dto.name())
                .address(dto.address())
                .cuisineType(dto.cuisineType())
                .openingHours(dto.openingHours())
                .owner(owner)
                .build();
    }
}
