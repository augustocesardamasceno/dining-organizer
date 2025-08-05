package br.com.fiap.dining_organizer.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "usuario_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "usuarioTipo")
@Builder
public class Usuario {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;
    @Column(name = "address")
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_tipo_id",
            foreignKey = @ForeignKey(name = "fk_usuario_usuario_tipo"))
    private UsuarioTipo usuarioTipo;
}
