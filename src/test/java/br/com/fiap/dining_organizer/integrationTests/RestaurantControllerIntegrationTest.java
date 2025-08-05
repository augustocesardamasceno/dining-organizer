package br.com.fiap.dining_organizer.integrationTests;

import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.UpdateRestaurantDto;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioTipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RestaurantControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioTipoRepository usuarioTipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Long ownerId;

    @BeforeEach
    void setupOwner() {
        UsuarioTipo tipo = UsuarioTipo.builder()
                .code(UsuarioTipoCode.ADMIN)
                .description("Proprietário de restaurante")
                .build();
        tipo = usuarioTipoRepository.save(tipo);

        Usuario owner = Usuario.builder()
                .name("Owner Test")
                .email("owner@example.com")
                .address("Rua Teste 123")
                .login("ownerlogin")
                .password("secret")
                .usuarioTipo(tipo)
                .build();
        owner = usuarioRepository.save(owner);
        ownerId = owner.getId();
    }

    @Test
    void shouldCreateGetUpdateAndDeleteRestaurant() {
        CreateRestaurantDto createDto = new CreateRestaurantDto(
                "Restaurante A",
                "Av. Principal, 100",
                "Italiano",
                "09:00-22:00",
                ownerId
        );
        ResponseEntity<RestaurantDto> createResponse = restTemplate.postForEntity(
                "/restaurants", createDto, RestaurantDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        RestaurantDto created = createResponse.getBody();
        assertEquals("Restaurante A", created.name());
        assertEquals(ownerId, created.ownerId());
        URI location = createResponse.getHeaders().getLocation();
        assertNotNull(location);

        String path = location.getPath();
        long restaurantId = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

        ResponseEntity<RestaurantDto> getResponse = restTemplate.getForEntity(
                "/restaurants/" + restaurantId, RestaurantDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Restaurante A", getResponse.getBody().name());

        ResponseEntity<Map> listResponse = restTemplate.getForEntity(
                "/restaurants", Map.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertTrue(((Map<?, ?>) listResponse.getBody()).containsKey("content"));

        UpdateRestaurantDto updateDto = new UpdateRestaurantDto();
        updateDto.setName("Restaurante A Atualizado");
        updateDto.setAddress("Av. Nova, 200");
        updateDto.setCuisineType("Mexicano");
        updateDto.setOpeningHours("10:00-23:00");
        updateDto.setOwnerId(ownerId);

        HttpEntity<UpdateRestaurantDto> updateEntity = new HttpEntity<>(updateDto);
        ResponseEntity<RestaurantDto> updateResponse = restTemplate.exchange(
                "/restaurants/" + restaurantId,
                HttpMethod.PUT,
                updateEntity,
                RestaurantDto.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Restaurante A Atualizado", updateResponse.getBody().name());
        assertEquals("Mexicano", updateResponse.getBody().cuisineType());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/restaurants/" + restaurantId + "?ownerId=" + ownerId,
                HttpMethod.DELETE,
                null,
                Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<RestaurantDto> getAfterDelete = restTemplate.getForEntity(
                "/restaurants/" + restaurantId, RestaurantDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}
