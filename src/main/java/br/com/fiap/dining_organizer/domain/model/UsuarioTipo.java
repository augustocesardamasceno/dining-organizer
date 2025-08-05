package br.com.fiap.dining_organizer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuario_tipo_tb", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuario_tipo_code", columnNames = "code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioTipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private UsuarioTipoCode code;

    @Column(name = "description")
    private String description;
}
