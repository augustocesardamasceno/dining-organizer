package br.com.fiap.dining_organizer.application.services.menuItem;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.UpdateMenuItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MenuItemService {
    MenuItemDto create(CreateMenuItemDto dto);
    Page<MenuItemDto> findAll(Pageable pageable);
    Optional<MenuItemDto> findById(Long id);
    Optional<MenuItemDto> update(Long id, UpdateMenuItemDto dto);
    boolean delete(Long id, Long ownerId);
}
