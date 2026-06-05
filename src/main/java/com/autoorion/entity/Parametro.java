package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros",
    indexes = { @Index(name = "idx_param_grupo_id", columnList = "grupo_id") })
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Parametro extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true, length = 80)
    private String nome;        // ex: prmArredondamento
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "grupo_id", nullable = false, length = 36)
    private String grupoId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String valor;
    @Column(nullable = false, length = 20)
    private String tipo;        // texto | numero | booleano | lista
    @Column(columnDefinition = "TEXT")
    private String opcoes;      // JSON array as string: ["opt1","opt2"]
    @Column(nullable = false)
    private Integer ordem = 1;
}
