package br.com.fiap.dining_organizer.entities;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Usuario {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Date lastUpdate;
    private String adress;
}
