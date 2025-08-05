package br.com.fiap.dining_organizer.infrasctructure.repository;

import br.com.fiap.dining_organizer.domain.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}


