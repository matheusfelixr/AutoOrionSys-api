package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Perfil de acesso do sistema.
 * Define quais telas (screenNames) um grupo de usuÃ¡rios pode acessar.
 * Gerenciado via ConfiguraÃ§Ãµes â†’ Perfis.
 */
@Entity
@Table(name = "perfis_acesso")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class PerfilAcesso extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Identificador Ãºnico do perfil (ex: admin, gerente, tecnico) */
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    /**
     * Lista de screenNames permitidos â€” armazenada como JSON no banco.
     * Ex: ["dashboard","usuarios","obras"]
     * Usada pelo frontend para controlar quais telas o usuÃ¡rio vÃª.
     */
    @Column(name = "telas_permitidas", columnDefinition = "TEXT")
    private String telasPermitidas; // JSON array as string

    private Integer totalUsuarios = 0;
}
