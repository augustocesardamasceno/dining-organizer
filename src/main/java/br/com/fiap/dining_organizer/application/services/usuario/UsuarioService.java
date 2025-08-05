package br.com.fiap.dining_organizer.application.services.usuario;

import br.com.fiap.dining_organizer.application.dtos.usuario.ChangePasswordDto;
import br.com.fiap.dining_organizer.application.dtos.usuario.UpdateUsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.usuario.UsuarioDto;
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
