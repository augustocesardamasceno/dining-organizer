package br.com.fiap.dining_organizer.infrasctructure.repository;

import br.com.fiap.dining_organizer.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByLogin(String login);
    boolean existsByLogin(String login);
}
