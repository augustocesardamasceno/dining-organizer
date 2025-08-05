package br.com.fiap.dining_organizer.integrationTests;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.UpdateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.usuario.UsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MenuItemControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long ownerId;

    private Long createRestaurant() {
        UsuarioTipoDto tipoDto = new UsuarioTipoDto(null, UsuarioTipoCode.ADMIN, "Proprietário");
        restTemplate.postForEntity("/usuario-tipo", tipoDto, UsuarioTipoDto.class);

        UsuarioDto usuarioDto = new UsuarioDto(
                "Restaurant Owner",
                "owner@example.com",
                "Owner Address",
                "owner",
                "password123",
                UsuarioTipoCode.ADMIN
        );
        ResponseEntity<UsuarioDto> usuarioResponse = restTemplate.postForEntity(
                "/usuarios", usuarioDto, UsuarioDto.class);
        URI usuarioLocation = usuarioResponse.getHeaders().getLocation();
        ownerId = Long.parseLong(usuarioLocation.getPath()
                .substring(usuarioLocation.getPath().lastIndexOf('/') + 1));

        CreateRestaurantDto restaurantDto = new CreateRestaurantDto(
                "My Restaurant",
                "123 Main St",
                "Italian",
                "08:00-22:00",
                ownerId
        );
        ResponseEntity<RestaurantDto> restaurantResponse = restTemplate.postForEntity(
                "/restaurants", restaurantDto, RestaurantDto.class);
        URI restaurantLocation = restaurantResponse.getHeaders().getLocation();
        return Long.parseLong(restaurantLocation.getPath()
                .substring(restaurantLocation.getPath().lastIndexOf('/') + 1));
    }

    @Test
    void shouldCreateGetUpdateAndDeleteMenuItem() {
        Long restaurantId = createRestaurant();

        CreateMenuItemDto createDto = new CreateMenuItemDto(
                "Spaghetti Carbonara",
                "Traditional Italian pasta",
                BigDecimal.valueOf(25.90),
                true,
                "/photos/carbonara.jpg",
                restaurantId
        );

        ResponseEntity<MenuItemDto> createResponse = restTemplate.postForEntity(
                "/menu-items", createDto, MenuItemDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        URI location = createResponse.getHeaders().getLocation();
        Long menuItemId = Long.parseLong(location.getPath()
                .substring(location.getPath().lastIndexOf('/') + 1));

        ResponseEntity<MenuItemDto> getResponse = restTemplate.getForEntity(
                "/menu-items/" + menuItemId, MenuItemDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(menuItemId, getResponse.getBody().id());

        UpdateMenuItemDto updateDto = new UpdateMenuItemDto();
        updateDto.setName("Updated Carbonara");
        updateDto.setDescription("Improved recipe");
        updateDto.setPrice(BigDecimal.valueOf(27.50));
        updateDto.setDineInOnly(false);
        updateDto.setPhotoPath("/photos/new-carbonara.jpg");
        updateDto.setRestaurantId(restaurantId);
        updateDto.setOwnerId(ownerId);

        ResponseEntity<MenuItemDto> updateResponse = restTemplate.exchange(
                "/menu-items/" + menuItemId,
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                MenuItemDto.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Updated Carbonara", updateResponse.getBody().name());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/menu-items/" + menuItemId + "?ownerId=" + ownerId,
                HttpMethod.DELETE,
                null,
                Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<MenuItemDto> getAfterDelete = restTemplate.getForEntity(
                "/menu-items/" + menuItemId, MenuItemDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}