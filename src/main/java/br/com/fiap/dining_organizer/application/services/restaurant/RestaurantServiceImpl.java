package br.com.fiap.dining_organizer.application.services.restaurant;

import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantMapper;
import br.com.fiap.dining_organizer.application.dtos.restaurant.UpdateRestaurantDto;
import br.com.fiap.dining_organizer.domain.model.Restaurant;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import br.com.fiap.dining_organizer.infrasctructure.repository.RestaurantRepository;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public RestaurantDto create(CreateRestaurantDto dto) {
        Usuario owner = usuarioRepository.findById(dto.ownerId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dono não existe"));

        if (owner.getUsuarioTipo().getCode() != UsuarioTipoCode.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente administradores podem criar restaurantes");
        }

        Restaurant saved = repository.save(mapper.toEntity(dto, owner));
        return mapper.toDto(saved);
    }


    @Override
    public Page<RestaurantDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Optional<RestaurantDto> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Optional<RestaurantDto> update(Long id, UpdateRestaurantDto dto) {

        if (dto.getOwnerId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O ownerId é obrigatório para atualização");
        }
        return repository.findById(id).map(existing -> {
            if (!existing.getOwner().getId().equals(dto.getOwnerId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Somente o dono pode atualizar este restaurante");
            }
            if (dto.getName() != null) existing.setName(dto.getName());
            if (dto.getAddress() != null) existing.setAddress(dto.getAddress());
            if (dto.getCuisineType() != null) existing.setCuisineType(dto.getCuisineType());
            if (dto.getOpeningHours() != null) existing.setOpeningHours(dto.getOpeningHours());
            if (dto.getOwnerId() != null) {
                Usuario newOwner = usuarioRepository.findById(dto.getOwnerId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dono não existe"));
                existing.setOwner(newOwner);
            }
            return mapper.toDto(repository.save(existing));
        });
    }

    @Override
    public boolean delete(Long id, Long ownerId) {
        return repository.findById(id).map(existing -> {
            if (!existing.getOwner().getId().equals(ownerId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Somente o dono pode apagar este restaurante");
            }
            repository.delete(existing);
            return true;
        }).orElse(false);
    }
}
