package br.com.fiap.dining_organizer.application.dtos.usuario;

import br.com.fiap.dining_organizer.domain.model.Usuario;
import br.com.fiap.dining_organizer.domain.model.UsuarioTipoCode;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioTipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final UsuarioTipoRepository usuarioTipoRepository;

    public Usuario mapToUsuario(UsuarioDto usuarioDto) {
        var usuarioTipo = usuarioTipoRepository.findByCode(usuarioDto.usuarioTipoCode()).orElseThrow(() -> new IllegalArgumentException("Tipo de usuário inexistente: " + usuarioDto.usuarioTipoCode()));
        return Usuario.builder()
                .name(usuarioDto.name())
                .email(usuarioDto.email())
                .address(usuarioDto.adress())
                .login(usuarioDto.login())
                .password(usuarioDto.password())
                .usuarioTipo(usuarioTipo)
                .build();
    }

    public UsuarioDto mapToUsuarioDto(Usuario usuario) {

        UsuarioTipoCode code = usuario.getUsuarioTipo() != null
                ? usuario.getUsuarioTipo().getCode()
                : null;

        return new UsuarioDto(
                usuario.getName(),
                usuario.getEmail(),
                usuario.getAddress(),
                usuario.getLogin(),
                null,
                code
        );
    }

}
