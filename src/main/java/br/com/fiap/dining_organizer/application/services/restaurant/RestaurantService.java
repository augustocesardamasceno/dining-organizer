package br.com.fiap.dining_organizer.application.services.restaurant;


import br.com.fiap.dining_organizer.application.dtos.restaurant.CreateRestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.RestaurantDto;
import br.com.fiap.dining_organizer.application.dtos.restaurant.UpdateRestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantService {
    RestaurantDto create(CreateRestaurantDto dto);
    Page<RestaurantDto> findAll(Pageable pageable);
    Optional<RestaurantDto> findById(Long id);
    Optional<RestaurantDto> update(Long id, UpdateRestaurantDto dto);
    boolean delete(Long id, Long ownerId);}
