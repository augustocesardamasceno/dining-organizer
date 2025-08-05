package br.com.fiap.dining_organizer.restaurant;

import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantMapper;
import br.com.fiap.dining_organizer.application.dtos.restaurant.UpdateRestaurantDto;
import br.com.fiap.dining_organizer.application.services.restaurant.RestaurantServiceImpl;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository repository;

    @Mock
    private RestaurantMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RestaurantServiceImpl service;

    @Test
    void create_shouldReturnDto_whenHappyPath() {
        Long ownerId = 99L;
        CreateRestaurantDto dto = new CreateRestaurantDto("Nome", "Endereço", "Italiana", "10:00-22:00", ownerId);
        Usuario owner = mock(Usuario.class);
        // admin check
        UsuarioTipo tipo = mock(UsuarioTipo.class);
        when(tipo.getCode()).thenReturn(UsuarioTipoCode.ADMIN);
        when(owner.getUsuarioTipo()).thenReturn(tipo);

        Restaurant entity = Restaurant.builder()
                .name("Nome")
                .address("Endereço")
                .cuisineType("Italiana")
                .openingHours("10:00-22:00")
                .owner(owner)
                .build();
        Restaurant saved = Restaurant.builder()
                .id(1L)
                .name("Nome")
                .address("Endereço")
                .cuisineType("Italiana")
                .openingHours("10:00-22:00")
                .owner(owner)
                .build();
        RestaurantDto expected = new RestaurantDto(1L, "Nome", "Endereço", "Italiana", "10:00-22:00", ownerId);

        when(usuarioRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(mapper.toEntity(dto, owner)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(expected);

        RestaurantDto actual = service.create(dto);

        assertEquals(expected, actual);
        verify(usuarioRepository).findById(ownerId);
        verify(mapper).toEntity(dto, owner);
        verify(repository).save(entity);
        verify(mapper).toDto(saved);
    }

    @Test
    void create_shouldThrow_whenOwnerNotFound() {
        Long ownerId = 1234L;
        CreateRestaurantDto dto = new CreateRestaurantDto("Nome", "Endereço", "Italiana", "10:00-22:00", ownerId);
        when(usuarioRepository.findById(ownerId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(dto));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void findAll_shouldReturnPagedDtos() {
        Pageable pageable = PageRequest.of(0, 5);
        Restaurant r = Restaurant.builder()
                .id(2L)
                .name("X")
                .address("Y")
                .cuisineType("T")
                .openingHours("H")
                .build();
        RestaurantDto dto = new RestaurantDto(2L, "X", "Y", "T", "H", 11L);
        Page<Restaurant> page = new PageImpl<>(java.util.List.of(r));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(r)).thenReturn(dto);

        Page<RestaurantDto> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());
        Optional<RestaurantDto> opt = service.findById(5L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void update_partialFields_withOwner() {
        Long id = 3L;
        Long ownerId = 300L;
        Usuario owner = mock(Usuario.class);
        when(owner.getId()).thenReturn(ownerId);

        Restaurant existing = Restaurant.builder()
                .id(id)
                .name("Old")
                .address("OldAddr")
                .cuisineType("Antiga")
                .openingHours("09:00-18:00")
                .owner(owner)
                .build();
        UpdateRestaurantDto dto = new UpdateRestaurantDto();
        dto.setOwnerId(ownerId);
        dto.setName("Novo");
        dto.setCuisineType("Moderna");

        Restaurant updatedEntity = Restaurant.builder()
                .id(id)
                .name("Novo")
                .address("OldAddr")
                .cuisineType("Moderna")
                .openingHours("09:00-18:00")
                .owner(owner)
                .build();
        RestaurantDto expected = new RestaurantDto(id, "Novo", "OldAddr", "Moderna", "09:00-18:00", ownerId);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(usuarioRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(repository.save(existing)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(expected);

        Optional<RestaurantDto> result = service.update(id, dto);

        assertTrue(result.isPresent());
        assertEquals("Novo", result.get().name());
        assertEquals("Moderna", result.get().cuisineType());
        assertEquals(ownerId, result.get().ownerId());
    }

    @Test
    void delete_returnsTrue_whenOwnerMatches() {
        Long id = 7L;
        Long ownerId = 700L;
        Usuario owner = mock(Usuario.class);
        when(owner.getId()).thenReturn(ownerId);
        Restaurant existing = Restaurant.builder()
                .id(id)
                .owner(owner)
                .build();

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