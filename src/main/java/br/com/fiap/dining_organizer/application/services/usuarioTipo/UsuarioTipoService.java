package br.com.fiap.dining_organizer.application.services.usuarioTipo;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioTipoService {
    UsuarioTipoDto create(UsuarioTipoDto dto);
    Page<UsuarioTipoDto> findAll(Pageable pageable);
    Optional<UsuarioTipoDto> findById(Long id);
    Optional<UsuarioTipoDto> update(Long id, UsuarioTipoDto dto);
    boolean delete(Long id);
}
