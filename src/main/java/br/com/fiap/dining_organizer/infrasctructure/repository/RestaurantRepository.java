package br.com.fiap.dining_organizer.infrasctructure.repository;

import br.com.fiap.dining_organizer.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
