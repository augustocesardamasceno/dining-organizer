package br.com.fiap.dining_organizer.usuario;

import br.com.fiap.dining_organizer.application.dtos.usuario.UpdateUsuarioDto;
import br.com.fiap.dining_organizer.application.services.usuario.UsuarioServiceImpl;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import br.com.fiap.dining_organizer.application.exception.LoginDuplicadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl service;

    @Test
    void save_shouldEncodePasswordAndPersist_whenLoginUnique() {
        Usuario input = Usuario.builder()
                .login("user1")
                .password("plain")
                .name("Nome")
                .email("e@e.com")
                .address("Rua")
                .build();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("plain");

        Usuario afterSave = Usuario.builder()
                .id(10L)
                .login("user1")
                .password(encodedPassword)
                .name("Nome")
                .email("e@e.com")
                .address("Rua")
                .lastUpdate(new Date())
                .build();

        when(usuarioRepository.existsByLogin("user1")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario arg = invocation.getArgument(0);
            arg.setId(10L);
            return arg;
        });
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(afterSave));

        Usuario result = service.save(input);
        assertNotNull(result.getId());
        assertEquals("user1", result.getLogin());
        assertNotEquals("plain", result.getPassword()); // agora vai passar porque é codificada
        assertTrue(encoder.matches("plain", result.getPassword())); // valida que é equivalente
    }

    @Test
    void save_shouldThrow_whenLoginDuplicado() {
        Usuario input = Usuario.builder().login("user1").password("x").build();
        when(usuarioRepository.existsByLogin("user1")).thenReturn(true);
        assertThrows(LoginDuplicadoException.class, () -> service.save(input));
    }

    @Test
    void updateUsuario_shouldThrow_whenPasswordAtualIncorreta() {
        Usuario existing = Usuario.builder()
                .id(1L)
                .login("login")
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("senha_correta"))
                .build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existing));

        UpdateUsuarioDto dto = new UpdateUsuarioDto();
        dto.setPassword("errada");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.updateUsuario(1L, dto));
        assertEquals(401, ex.getStatusCode().value());
    }
}
