package com.flexsys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "usuarios",
    indexes = {
        @Index(name = "idx_usuario_email",  columnList = "email"),
        @Index(name = "idx_usuario_perfil", columnList = "perfil"),
        @Index(name = "idx_usuario_status", columnList = "status"),
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Usuario extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status;

    private String telefone;

    /** URL ou base64 do avatar do usuário */
    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    public enum PerfilUsuario { admin, gerente, tecnico, visualizador }
    public enum StatusUsuario { ativo, inativo }
}
