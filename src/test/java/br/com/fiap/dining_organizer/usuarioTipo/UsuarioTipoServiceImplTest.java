package br.com.fiap.dining_organizer.usuarioTipo;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoMapper;
import br.com.fiap.dining_organizer.application.services.usuarioTipo.UsuarioTipoServiceImpl;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioTipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioTipoServiceImplTest {

    @Mock
    private UsuarioTipoRepository repository;

    @Mock
    private UsuarioTipoMapper mapper;

    @InjectMocks
    private UsuarioTipoServiceImpl service;

    @Test
    void create_shouldSave_whenCodeUnique(){
        UsuarioTipoDto dto = new UsuarioTipoDto(null, UsuarioTipoCode.CLIENT, "Cliente comum");
        UsuarioTipo entity = UsuarioTipo.builder().code(UsuarioTipoCode.valueOf("CLIENT")).description("Cliente comum").build();
        UsuarioTipo saved = UsuarioTipo.builder().id(1L).code(UsuarioTipoCode.valueOf("CLIENT")).description("Cliente comum").build();
        UsuarioTipoDto expected = new UsuarioTipoDto(1L, UsuarioTipoCode.CLIENT, "Cliente comum");

        when(repository.existsByCode(UsuarioTipoCode.CLIENT)).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(any(UsuarioTipo.class))).thenReturn(expected);

        UsuarioTipoDto actual = service.create(dto);
        assertEquals(expected, actual);
        verify(repository).save(entity);
    }

    @Test
    void create_shouldThrow_whenCodeAlreadyExists() {
        UsuarioTipoDto dto = new UsuarioTipoDto(null, UsuarioTipoCode.CLIENT, "Cliente comum");
        when(repository.existsByCode(UsuarioTipoCode.CLIENT)).thenReturn(true);
        assertThrows(Exception.class, () -> service.create(dto));
    }

    @Test
    void delete_shouldReturnFalse_whenNotFound() {
        when(repository.existsById(999L)).thenReturn(false);
        assertFalse(service.delete(999L));
    }

    @Test
    void delete_shouldReturnTrue_whenExists() {
        when(repository.existsById(5L)).thenReturn(true);
        assertTrue(service.delete(5L));
        verify(repository).deleteById(5L);
    }
}

