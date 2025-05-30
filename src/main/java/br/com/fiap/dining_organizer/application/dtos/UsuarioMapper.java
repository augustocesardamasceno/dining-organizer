package br.com.fiap.dining_organizer.application.dtos;

import br.com.fiap.dining_organizer.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario mapToUsuario(UsuarioDto usuarioDto) {
        return Usuario.builder()
                .name(usuarioDto.name())
                .email(usuarioDto.email())
                .adress(usuarioDto.adress())
                .login(usuarioDto.login())
                .password(usuarioDto.password())
                .build();
    }

    public UsuarioDto mapToUsuarioDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getName(),
                usuario.getEmail(),
                usuario.getAdress(),
                usuario.getLogin(),
                null
        );
    }

}
