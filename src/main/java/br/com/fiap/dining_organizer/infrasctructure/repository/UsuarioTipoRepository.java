package br.com.fiap.dining_organizer.infrasctructure.repository;

import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioTipoRepository extends JpaRepository<UsuarioTipo, Long> {
    boolean existsByCode(UsuarioTipoCode code);
    Optional<UsuarioTipo> findByCode(UsuarioTipoCode code);
}

