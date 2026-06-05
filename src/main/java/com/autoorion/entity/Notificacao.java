package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @Column(nullable = false)
    @Builder.Default
    private String tipo = "info"; // info, success, warning, error

    @Column(nullable = false)
    @Builder.Default
    private Boolean lida = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now();
}
