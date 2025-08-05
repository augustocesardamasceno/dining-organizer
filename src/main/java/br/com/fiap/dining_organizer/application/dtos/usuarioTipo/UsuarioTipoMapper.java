package br.com.fiap.dining_organizer.application.dtos.usuarioTipo;

import br.com.fiap.dining_organizer.domain.model.UsuarioTipo;
import org.springframework.stereotype.Component;

@Component
public class UsuarioTipoMapper {
    public UsuarioTipoDto toDto(UsuarioTipo usuarioTipo){
        return new UsuarioTipoDto(usuarioTipo.getId(), usuarioTipo.getCode(), usuarioTipo.getDescription());
    }

    public UsuarioTipo toEntity(UsuarioTipoDto usuarioTipoDto){
        return UsuarioTipo.builder()
                .id(usuarioTipoDto.id())
                .code(usuarioTipoDto.code())
                .description(usuarioTipoDto.description())
                .build();
    }
}
