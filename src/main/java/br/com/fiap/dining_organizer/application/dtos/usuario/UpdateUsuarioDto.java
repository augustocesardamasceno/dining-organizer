package br.com.fiap.dining_organizer.application.dtos.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUsuarioDto {

    @NotBlank(message = "A senha atual é obrigatória")
    private String password;

    private String login;
    private String name;
    private String email;
    private String adress;

    private Long usuarioTipoId;

    public void setUsuarioTipoId(Long usuarioTipoId){ this.usuarioTipoId = usuarioTipoId; }

    public void setPassword(String password) { this.password = password; }

    public void setLogin(String login) { this.login = login; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setAdress(String adress) { this.adress = adress; }
}
