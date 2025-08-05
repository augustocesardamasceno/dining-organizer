package br.com.fiap.dining_organizer.menuItem;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemMapper;
import br.com.fiap.dining_organizer.application.dtos.menu.UpdateMenuItemDto;
import br.com.fiap.dining_organizer.application.services.menuItem.MenuItemServiceImpl;
import br.com.fiap.dining_organizer.domain.model.MenuItem;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.infrasctructure.repository.MenuItemRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository repository;

    @Mock
    private MenuItemMapper mapper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemServiceImpl service;

    @Test
    void create_shouldReturnDto_whenHappyPath() {
        Long restaurantId = 42L;
        CreateMenuItemDto dto = new CreateMenuItemDto("Lasanha", "Deliciosa", BigDecimal.valueOf(30), true, "/img.jpg", restaurantId);
        Restaurant restaurant = Restaurant.builder().id(restaurantId).build();

        MenuItem entity = MenuItem.builder()
                .name("Lasanha")
                .description("Deliciosa")
                .price(BigDecimal.valueOf(30))
                .dineInOnly(true)
                .photoPath("/img.jpg")
                .restaurant(restaurant)
                .build();

        MenuItem saved = MenuItem.builder()
                .id(5L)
                .name("Lasanha")
                .description("Deliciosa")
                .price(BigDecimal.valueOf(30))
                .dineInOnly(true)
                .photoPath("/img.jpg")
                .restaurant(restaurant)
                .build();

        MenuItemDto expected = new MenuItemDto(5L, "Lasanha", "Deliciosa", BigDecimal.valueOf(30), true, "/img.jpg", restaurantId);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(mapper.toEntity(dto, restaurant)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(expected);

        MenuItemDto actual = service.create(dto);

        assertEquals(expected, actual);
        verify(restaurantRepository).findById(restaurantId);
        verify(repository).save(entity);
    }

    @Test
    void findAll_returnsPagedResults() {
        Pageable pageable = PageRequest.of(0, 5);
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItem m = MenuItem.builder()
                .id(2L)
                .name("X")
                .description("D")
                .price(BigDecimal.valueOf(10))
                .dineInOnly(false)
                .photoPath("/p")
                .restaurant(restaurant)
                .build();
        MenuItemDto dto = new MenuItemDto(2L, "X", "D", BigDecimal.valueOf(10), false, "/p", 1L);
        Page<MenuItem> page = new PageImpl<>(java.util.List.of(m));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(m)).thenReturn(dto);

        Page<MenuItemDto> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void findById_emptyWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<MenuItemDto> opt = service.findById(99L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void update_partialFields_withoutRestaurantChange() {
        Long id = 10L;
        Long restaurantId = 1L;
        Restaurant restaurant = Restaurant.builder().id(restaurantId).build();
        MenuItem existing = MenuItem.builder()
                .id(id)
                .name("Old")
                .description("Desc")
                .price(BigDecimal.valueOf(20))
                .dineInOnly(false)
                .photoPath("/old.jpg")
                .restaurant(restaurant)
                .build();
        UpdateMenuItemDto dto = new UpdateMenuItemDto();
        dto.setRestaurantId(restaurantId);
        dto.setName("Novo");
        dto.setPrice(BigDecimal.valueOf(25));

        MenuItem updated = MenuItem.builder()
                .id(id)
                .name("Novo")
                .description("Desc")
                .price(BigDecimal.valueOf(25))
                .dineInOnly(false)
                .photoPath("/old.jpg")
                .restaurant(restaurant)
                .build();
        MenuItemDto expected = new MenuItemDto(id, "Novo", "Desc", BigDecimal.valueOf(25), false, "/old.jpg", restaurantId);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(expected);

        Optional<MenuItemDto> result = service.update(id, dto);

        assertTrue(result.isPresent());
        assertEquals("Novo", result.get().name());
        assertEquals(BigDecimal.valueOf(25), result.get().price());
        assertEquals(restaurantId, result.get().restaurantId());
    }

    @Test
    void delete_returnsTrue_whenOwnerMatches() {
        Long id = 7L;
        Long ownerId = 700L;
        Usuario owner = mock(Usuario.class);
        when(owner.getId()).thenReturn(ownerId);
        Restaurant restaurant = Restaurant.builder().owner(owner).build();
        MenuItem existing = MenuItem.builder().id(id).restaurant(restaurant).build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        assertTrue(service.delete(id, ownerId));
        verify(repository).delete(existing);
    }

    @Test
    void delete_returnsFalse_whenNotFound() {
        Long id = 8L;
        Long ownerId = 800L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertFalse(service.delete(id, ownerId));
    }
}
