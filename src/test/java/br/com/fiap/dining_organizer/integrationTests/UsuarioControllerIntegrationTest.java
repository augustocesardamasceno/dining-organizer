package br.com.fiap.dining_organizer.integrationTests;

import br.com.fiap.dining_organizer.application.dtos.usuario.UpdateUsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.usuario.UsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsuarioControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long usuarioTipoId;

    @Autowired
    private org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping mapping;

    @Test
    void dumpAllMappings() {
        mapping.getHandlerMethods().forEach((info, method) -> {
            System.out.println("Mapping: " + info + " -> " + method.getMethod());
        });
    }

    @Test
    void shouldCreateGetUpdateAndDeleteUsuario() {

        UsuarioTipoDto tipoDto = new UsuarioTipoDto(null, UsuarioTipoCode.CLIENT, "Cliente");
        ResponseEntity<UsuarioTipoDto> response = restTemplate.postForEntity(
                "/usuario-tipo", tipoDto, UsuarioTipoDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        usuarioTipoId = response.getBody().id();

        UsuarioDto createDto = new UsuarioDto(
                "John Doe",
                "john@example.com",
                "123 Main St",
                "johndoe",
                "password123",
                UsuarioTipoCode.CLIENT
        );

        ResponseEntity<UsuarioDto> createResponse = restTemplate.postForEntity("/usuarios", createDto, UsuarioDto.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals("johndoe", createResponse.getBody().login());

        URI location = createResponse.getHeaders().getLocation();
        assertNotNull(location);
        String path = location.getPath();
        long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

        Map<String, String> loginBody = Map.of(
                "login", "johndoe",
                "password", "password123"
        );
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/usuarios/login", loginBody, Map.class);
        String token = (String) loginResponse.getBody().get("token"); // ajuste conforme o campo real

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> authEntity = new HttpEntity<>(headers);

        ResponseEntity<UsuarioDto> getResponse = restTemplate.exchange(
                "/usuarios/" + id,
                HttpMethod.GET,
                authEntity,
                UsuarioDto.class);


        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/usuarios/" + id,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<UsuarioDto> getAfterDelete = restTemplate.getForEntity(
                "/usuarios/" + id, UsuarioDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }

}
