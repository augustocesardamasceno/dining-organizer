package br.com.fiap.dining_organizer.application.services.menuItem;

import br.com.fiap.dining_organizer.application.dtos.menu.CreateMenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemDto;
import br.com.fiap.dining_organizer.application.dtos.menu.MenuItemMapper;
import br.com.fiap.dining_organizer.application.dtos.menu.UpdateMenuItemDto;
import br.com.fiap.dining_organizer.domain.model.MenuItem;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.infrasctructure.repository.MenuItemRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository repository;
    private final MenuItemMapper mapper;
    private final RestaurantRepository restaurantRepository;

    @Override
    public MenuItemDto create(CreateMenuItemDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurante não existe"));
        MenuItem saved = repository.save(mapper.toEntity(dto, restaurant));
        return mapper.toDto(saved);
    }

    @Override
    public Page<MenuItemDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Optional<MenuItemDto> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Optional<MenuItemDto> update(Long id, UpdateMenuItemDto dto) {
        if (dto.getRestaurantId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "restaurantId é obrigatório para atualização");
        }
        return repository.findById(id).map(existing -> {
            if (dto.getName() != null) existing.setName(dto.getName());
            if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
            if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
            if (dto.getDineInOnly() != null) existing.setDineInOnly(dto.getDineInOnly());
            if (dto.getPhotoPath() != null) existing.setPhotoPath(dto.getPhotoPath());
            if (dto.getRestaurantId() != null) {
                Restaurant newRestaurant = restaurantRepository.findById(dto.getRestaurantId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurante não existe"));
                existing.setRestaurant(newRestaurant);
            }
            return mapper.toDto(repository.save(existing));
        });
    }

    public boolean delete(Long id, Long ownerId) {
        return repository.findById(id).map(existing -> {
            if (!existing.getRestaurant().getOwner().getId().equals(ownerId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Somente o dono pode apagar este menu");
            }
            repository.delete(existing);
            return true;
        }).orElse(false);
    }
}
