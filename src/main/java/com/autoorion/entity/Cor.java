package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Cor extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 60)
    private String nome;
}
