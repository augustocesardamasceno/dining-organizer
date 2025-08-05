package br.com.fiap.dining_organizer.application.services.usuarioTipo;

import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoDto;
import br.com.fiap.dining_organizer.application.dtos.usuarioTipo.UsuarioTipoMapper;
import br.com.fiap.dining_organizer.infrasctructure.repository.UsuarioTipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioTipoServiceImpl implements UsuarioTipoService{
    private final UsuarioTipoRepository usuarioTipoRepository;
    private final UsuarioTipoMapper usuarioTipoMapper;

    @Override
    public UsuarioTipoDto create(UsuarioTipoDto usuarioTipoDto){
        if (usuarioTipoRepository.existsByCode(usuarioTipoDto.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este tipo de usuário já existe");
        }
        var saved = usuarioTipoRepository.save(usuarioTipoMapper.toEntity(usuarioTipoDto));
        return usuarioTipoMapper.toDto(saved);
    }

    @Override
    public Optional<UsuarioTipoDto> update(Long id, UsuarioTipoDto dto) {
        return usuarioTipoRepository.findById(id).map(existing -> {
            if (!existing.getCode().equals(dto.code()) && usuarioTipoRepository.existsByCode(dto.code())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Este tipo de usuário já existe");
            }
            existing.setCode(dto.code());
            existing.setDescription(dto.description());
            return usuarioTipoMapper.toDto(usuarioTipoRepository.save(existing));
        });
    }


    @Override
    public Page<UsuarioTipoDto> findAll(Pageable pageable) {
        return usuarioTipoRepository.findAll(pageable).map(usuarioTipoMapper::toDto);
    }

    @Override
    public Optional<UsuarioTipoDto> findById(Long id) {
        return usuarioTipoRepository.findById(id).map(usuarioTipoMapper::toDto);
    }


    @Override
    public boolean delete(Long id) {
        if (usuarioTipoRepository.existsById(id)){
            usuarioTipoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
