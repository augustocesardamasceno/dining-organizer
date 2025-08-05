package br.com.fiap.dining_organizer.presentation.controllers;

import br.com.fiap.dining_organizer.application.dtos.usuario.*;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.application.services.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<UsuarioDto> create(@Valid @RequestBody UsuarioDto dto) {
        Usuario u = usuarioMapper.mapToUsuario(dto);
        Usuario saved = usuarioService.save(u);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location) // <-- isso seta o header Location
                .body(usuarioMapper.mapToUsuarioDto(saved));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuarioMapper::mapToUsuarioDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @GetMapping
    public Page<UsuarioDto> getAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "login", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return usuarioService.findAll(pageable);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsuarioDto dto) {

        Usuario updated = usuarioService.updateUsuario(id, dto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return ResponseEntity.ok(usuarioMapper.mapToUsuarioDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (usuarioService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@Valid @RequestBody LoginDto loginDto) {
        return usuarioService.validateLogin(loginDto.login(), loginDto.password())
                .map(usuarioMapper::mapToUsuarioDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto) {
        usuarioService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
