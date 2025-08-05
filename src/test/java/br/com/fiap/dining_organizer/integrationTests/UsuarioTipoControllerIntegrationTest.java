package br.com.fiap.dining_organizer.integrationTests;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
// força recriar o contexto (e a base) depois de cada classe de teste para evitar colisões
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsuarioTipoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private UsuarioTipoDto ensureAdminTipo() {
        UsuarioTipoDto createDto = new UsuarioTipoDto(null, UsuarioTipoCode.ADMIN, "Administrator");
        ResponseEntity<UsuarioTipoDto> createResponse = restTemplate.postForEntity(
                "/usuario-tipo", createDto, UsuarioTipoDto.class);

        if (createResponse.getStatusCode() == HttpStatus.CREATED) {
            return createResponse.getBody();
        } else if (createResponse.getStatusCode() == HttpStatus.CONFLICT) {
            // busca existente listando e filtrando pelo code
            ResponseEntity<Map> listResponse = restTemplate.getForEntity("/usuario-tipo?size=10", Map.class);
            assertEquals(HttpStatus.OK, listResponse.getStatusCode());
            Map<?, ?> body = listResponse.getBody();
            assertNotNull(body);
            List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
            return content.stream()
                    .filter(m -> "ADMIN".equals(m.get("code")))
                    .findFirst()
                    .map(m -> new UsuarioTipoDto(
                            ((Number) m.get("id")).longValue(),
                            UsuarioTipoCode.valueOf((String) m.get("code")),
                            (String) m.get("description")))
                    .orElseThrow(() -> new AssertionError("Tipo ADMIN não encontrado após CONFLICT"));
        } else {
            fail("Resposta inesperada ao garantir ADMIN: " + createResponse.getStatusCode());
            return null; // unreachable
        }
    }

    @Test
    void shouldCreateGetUpdateAndDeleteUsuarioTipo() {
        UsuarioTipoDto created = ensureAdminTipo();
        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals(UsuarioTipoCode.ADMIN, created.code());

        Long id = created.id();

        ResponseEntity<UsuarioTipoDto> getResponse = restTemplate.getForEntity(
                "/usuario-tipo/" + id, UsuarioTipoDto.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(id, getResponse.getBody().id());

        UsuarioTipoDto updateDto = new UsuarioTipoDto(id, UsuarioTipoCode.ADMIN, "Super Administrator");
        ResponseEntity<UsuarioTipoDto> updateResponse = restTemplate.exchange(
                "/usuario-tipo/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                UsuarioTipoDto.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(UsuarioTipoCode.ADMIN, updateResponse.getBody().code());
        assertEquals("Super Administrator", updateResponse.getBody().description());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/usuario-tipo/" + id,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<UsuarioTipoDto> getAfterDelete = restTemplate.getForEntity(
                "/usuario-tipo/" + id, UsuarioTipoDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}

