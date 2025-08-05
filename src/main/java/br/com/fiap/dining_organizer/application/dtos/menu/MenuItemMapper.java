package br.com.fiap.dining_organizer.application.dtos.menu;

import br.com.fiap.dining_organizer.domain.model.MenuItem;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemMapper {


    public MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getDineInOnly(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null
        );
    }

    public MenuItem toEntity(CreateMenuItemDto dto, Restaurant restaurant) {
        return MenuItem.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .dineInOnly(dto.dineInOnly())
                .photoPath(dto.photoPath())
                .restaurant(restaurant)
                .build();
    }
}