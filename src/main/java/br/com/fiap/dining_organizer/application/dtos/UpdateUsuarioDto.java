package br.com.fiap.dining_organizer.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateUsuarioDto {

    @NotBlank(message = "A senha atual é obrigatória")
    private String password;

    private String login;
    private String name;
    private String email;
    private String adress;

    // getters e setters
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdress() { return adress; }
    public void setAdress(String adress) { this.adress = adress; }
}
