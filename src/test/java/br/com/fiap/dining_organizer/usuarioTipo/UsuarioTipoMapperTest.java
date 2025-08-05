package br.com.fiap.dining_organizer.usuarioTipo;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoMapper;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsuarioTipoMapperTest {

    private final UsuarioTipoMapper mapper = new UsuarioTipoMapper();

    @Test
    void toDto_shouldConvertEntityToDto() {
        UsuarioTipo usuarioTipo = UsuarioTipo.builder()
                .id(1L)
                .code(UsuarioTipoCode.ADMIN)
                .description("Administrator")
                .build();

        UsuarioTipoDto dto = mapper.toDto(usuarioTipo);

        assertEquals(1L, dto.id());
        assertEquals(UsuarioTipoCode.ADMIN, dto.code());
        assertEquals("Administrator", dto.description());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        UsuarioTipoDto dto = new UsuarioTipoDto(1L, UsuarioTipoCode.CLIENT, "Cliente do restaurante");

        UsuarioTipo usuarioTipo = mapper.toEntity(dto);

        assertEquals(1L, usuarioTipo.getId());
        assertEquals(UsuarioTipoCode.CLIENT, usuarioTipo.getCode());
        assertEquals("Cliente do restaurante", usuarioTipo.getDescription());
    }
}
