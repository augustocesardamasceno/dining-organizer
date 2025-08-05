package br.com.fiap.dining_organizer.usuario;

import br.com.fiap.dining_organizer.application.dtos.usuario.UsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.usuario.UsuarioMapper;
import br.com.fiap.dining_organizer.domain.model.Usuario;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsuarioMapperTest {

    @Mock
    private UsuarioTipoRepository usuarioTipoRepository;

    @InjectMocks
    private UsuarioMapper mapper;

    @Test
    void mapToUsuario_shouldConvertDtoToEntity() {
        // Arrange
        UsuarioTipoCode tipoCode = UsuarioTipoCode.CLIENT;
        UsuarioTipo usuarioTipo = UsuarioTipo.builder().id(1L).code(tipoCode).build();
        UsuarioDto dto = new UsuarioDto(
                "John Doe",
                "john@example.com",
                "123 Main St",
                "johndoe",
                "password123",
                tipoCode
        );

        when(usuarioTipoRepository.findByCode(tipoCode)).thenReturn(Optional.of(usuarioTipo));

        // Act
        Usuario usuario = mapper.mapToUsuario(dto);

        // Assert
        assertEquals("John Doe", usuario.getName());
        assertEquals("john@example.com", usuario.getEmail());
        assertEquals("123 Main St", usuario.getAddress());
        assertEquals("johndoe", usuario.getLogin());
        assertEquals("password123", usuario.getPassword());
        assertSame(usuarioTipo, usuario.getUsuarioTipo());
    }

    @Test
    void mapToUsuario_shouldThrowWhenTipoNotFound() {
        // Arrange
        UsuarioTipoCode tipoCode = UsuarioTipoCode.CLIENT;
        UsuarioDto dto = new UsuarioDto(
                "John Doe",
                "john@example.com",
                "123 Main St",
                "johndoe",
                "password123",
                tipoCode
        );

        when(usuarioTipoRepository.findByCode(tipoCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.mapToUsuario(dto));
    }

    @Test
    void mapToUsuarioDto_shouldConvertEntityToDto() {
        // Arrange
        UsuarioTipo usuarioTipo = UsuarioTipo.builder().id(1L).code(UsuarioTipoCode.CLIENT).build();
        Usuario usuario = Usuario.builder()
                .name("John Doe")
                .email("john@example.com")
                .address("123 Main St")
                .login("johndoe")
                .password("password123")
                .usuarioTipo(usuarioTipo)
                .build();

        UsuarioDto dto = mapper.mapToUsuarioDto(usuario);

        assertEquals("John Doe", dto.name());
        assertEquals("john@example.com", dto.email());
        assertEquals("123 Main St", dto.adress());
        assertEquals("johndoe", dto.login());
        assertNull(dto.password());
        assertEquals(UsuarioTipoCode.CLIENT, dto.usuarioTipoCode());
    }
}