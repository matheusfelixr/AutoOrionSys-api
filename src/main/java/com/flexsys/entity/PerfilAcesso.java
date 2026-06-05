package com.flexsys.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Perfil de acesso do sistema.
 * Define quais telas (screenNames) um grupo de usuários pode acessar.
 * Gerenciado via Configurações → Perfis.
 */
@Entity
@Table(name = "perfis_acesso")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class PerfilAcesso extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Identificador único do perfil (ex: admin, gerente, tecnico) */
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    /**
     * Lista de screenNames permitidos — armazenada como JSON no banco.
     * Ex: ["dashboard","usuarios","obras"]
     * Usada pelo frontend para controlar quais telas o usuário vê.
     */
    @Column(name = "telas_permitidas", columnDefinition = "TEXT")
    private String telasPermitidas; // JSON array as string

    private Integer totalUsuarios = 0;
}
