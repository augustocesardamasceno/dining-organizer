package br.com.fiap.dining_organizer.presentation.controllers;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.application.services.usuarioTipo.UsuarioTipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario-tipo")
@RequiredArgsConstructor
public class UsuarioTipoController {
    private final UsuarioTipoService usuarioTipoService;

    @PostMapping
    public ResponseEntity<UsuarioTipoDto> create (@RequestBody UsuarioTipoDto usuarioTipoDto){
        return ResponseEntity.status(201).body(usuarioTipoService.create(usuarioTipoDto));
    }


    @GetMapping
    public ResponseEntity<?> list (Pageable pageable){
        return ResponseEntity.ok(usuarioTipoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioTipoDto> get(@PathVariable Long id){
        return usuarioTipoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioTipoDto> update (@PathVariable Long id, @RequestBody UsuarioTipoDto usuarioTipoDto){
        return usuarioTipoService.update(id, usuarioTipoDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return usuarioTipoService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
