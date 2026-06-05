package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telas_sistema",
    indexes = {
        @Index(name = "idx_tela_screen_name", columnList = "screen_name"),
        @Index(name = "idx_tela_menu_id", columnList = "menu_id"),
    })
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class TelaSistema extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "screen_name", nullable = false, unique = true, length = 80)
    private String screenName;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "menu_id", length = 36)
    private String menuId;
    @Column(name = "parent_screen_name", length = 80)
    private String parentScreenName;
    @Column(length = 10)
    private String icone;
    @Column(nullable = false)
    private Integer ordem = 1;
}
