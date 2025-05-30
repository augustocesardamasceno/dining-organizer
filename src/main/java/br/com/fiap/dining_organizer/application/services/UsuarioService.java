package br.com.fiap.dining_organizer.application.services;

import br.com.fiap.dining_organizer.application.dtos.ChangePasswordDto;
import br.com.fiap.dining_organizer.application.dtos.UpdateUsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.UsuarioDto;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> findById(Long id);
    Page<UsuarioDto> findAll(Pageable usuarioPageable);
    Usuario save(Usuario usuario);
    Optional<Usuario> updateUsuario(Long id, UpdateUsuarioDto dto);

    @Transactional
    boolean delete(Long id);

    Optional<Usuario> validateLogin(String login, String password);
    Optional<Usuario> changePassword(ChangePasswordDto dto);

}
