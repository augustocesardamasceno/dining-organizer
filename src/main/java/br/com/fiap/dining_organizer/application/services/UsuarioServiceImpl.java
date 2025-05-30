package br.com.fiap.dining_organizer.application.services;

import br.com.fiap.dining_organizer.application.dtos.ChangePasswordDto;
import br.com.fiap.dining_organizer.application.dtos.UpdateUsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.UsuarioDto;
import br.com.fiap.dining_organizer.application.dtos.UsuarioMapper;
import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.application.exception.LoginDuplicadoException;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Page<UsuarioDto> findAll(Pageable usuarioPageable) {
        return usuarioRepository.findAll(usuarioPageable)
                .map(usuarioMapper::mapToUsuarioDto);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByLogin(usuario.getLogin())) {
            throw new LoginDuplicadoException("Já existe um usuário com esse login");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setLastUpdate(new Date());
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioRepository.findById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Erro ao recarregar usuário"));
    }


    @Override
    @Transactional
    public Optional<Usuario> updateUsuario(Long id, UpdateUsuarioDto dto) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    if (!passwordEncoder.matches(dto.getPassword(), existing.getPassword())) {
                        throw new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Senha atual incorreta");
                    }

                    if (dto.getLogin() != null) {
                        if (usuarioRepository.existsByLogin(dto.getLogin())
                                && !dto.getLogin().equals(existing.getLogin())) {
                            throw new LoginDuplicadoException();
                        }
                        existing.setLogin(dto.getLogin());
                    }
                    if (dto.getName() != null) {
                        existing.setName(dto.getName());
                    }
                    if (dto.getEmail() != null) {
                        existing.setEmail(dto.getEmail());
                    }
                    if (dto.getAdress() != null) {
                        existing.setAdress(dto.getAdress());
                    }

                    existing.setLastUpdate(new Date());

                    return usuarioRepository.save(existing);
                });
    }


    @Transactional
    @Override
    public boolean delete(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Usuario> validateLogin(String login, String password) {
        return usuarioRepository.findByLogin(login)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Override
    @Transactional
    public Optional<Usuario> changePassword(ChangePasswordDto dto) {
        Usuario usuario = usuarioRepository
                .findByLogin(dto.login())
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));

        if (!passwordEncoder.matches(dto.oldPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Senha antiga incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.newPassword()));
        usuario.setLastUpdate(new Date());
        usuarioRepository.save(usuario);
        return null;
    }
}
