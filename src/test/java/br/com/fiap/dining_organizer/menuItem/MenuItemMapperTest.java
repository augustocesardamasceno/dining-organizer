package br.com.fiap.dining_organizer.menuItem;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemMapper;
import br.com.fiap.dining_organizer.domain.model.MenuItem;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemMapperTest {

    private final MenuItemMapper mapper = new MenuItemMapper();

    @Test
    void toDto_shouldConvertMenuItemToDto() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder()
                .id(100L)
                .name("Pizza")
                .description("Delicious pizza")
                .price(new BigDecimal("29.99"))
                .dineInOnly(true)
                .photoPath("/photos/pizza.jpg")
                .restaurant(restaurant)
                .build();

        MenuItemDto dto = mapper.toDto(menuItem);

        assertEquals(100L, dto.id());
        assertEquals("Pizza", dto.name());
        assertEquals("Delicious pizza", dto.description());
        assertEquals(new BigDecimal("29.99"), dto.price());
        assertTrue(dto.dineInOnly());
        assertEquals("/photos/pizza.jpg", dto.photoPath());
        assertEquals(1L, dto.restaurantId());
    }

    @Test
    void toEntity_shouldConvertDtoToMenuItem() {
        Restaurant restaurant = Restaurant.builder().id(2L).build();

        CreateMenuItemDto dto = new CreateMenuItemDto(
                "Burger",
                "Tasty burger",
                new BigDecimal("19.99"),
                false,
                "/photos/burger.jpg",
                restaurant.getId()

        );

        MenuItem menuItem = mapper.toEntity(dto, restaurant);

        assertNull(menuItem.getId());
        assertEquals("Burger", menuItem.getName());
        assertEquals("Tasty burger", menuItem.getDescription());
        assertEquals(new BigDecimal("19.99"), menuItem.getPrice());
        assertFalse(menuItem.getDineInOnly());
        assertEquals("/photos/burger.jpg", menuItem.getPhotoPath());
        assertSame(restaurant, menuItem.getRestaurant());
    }
}
