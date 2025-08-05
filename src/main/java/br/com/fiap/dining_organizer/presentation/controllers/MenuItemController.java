package br.com.fiap.dining_organizer.presentation.controllers;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.UpdateMenuItemDto;
import br.com.fiap.dining_organizer.application.services.menuItem.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService service;

    @PostMapping
    public ResponseEntity<MenuItemDto> create(@RequestBody CreateMenuItemDto dto) {
        MenuItemDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/menu-items/" + created.id())).body(created);
    }

    @GetMapping
    public ResponseEntity<?> list(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemDto dto) {

        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam("ownerId") Long ownerId) {

        return service.delete(id, ownerId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}