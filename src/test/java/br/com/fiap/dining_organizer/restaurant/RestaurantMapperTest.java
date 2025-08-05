package br.com.fiap.dining_organizer.restaurant;

import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantMapper;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestaurantMapperTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RestaurantMapper mapper;

    @Test
    void toEntity_shouldBuildRestaurant() {
        Long ownerId = 7L;
        CreateRestaurantDto dto = new CreateRestaurantDto("Nome", "End", "Italiana", "9-18", ownerId);
        Usuario owner = Usuario.builder().id(ownerId).build();

        Restaurant restaurant = mapper.toEntity(dto, owner);

        assertEquals("Nome", restaurant.getName());
        assertEquals("End", restaurant.getAddress());
        assertEquals("Italiana", restaurant.getCuisineType());
        assertEquals("9-18", restaurant.getOpeningHours());
        assertSame(owner, restaurant.getOwner());
    }
}

