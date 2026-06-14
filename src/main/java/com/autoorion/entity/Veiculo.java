package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(
    name = "veiculos",
    indexes = {
        @Index(name = "idx_veiculo_placa",  columnList = "placa"),
        @Index(name = "idx_veiculo_marca",  columnList = "marca"),
        @Index(name = "idx_veiculo_baixado",columnList = "baixado"),
        @Index(name = "idx_veiculo_ativo",  columnList = "ativo"),
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Veiculo extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(name = "ano_fabricacao")
    private Integer anoFabricacao;

    /** Ano do modelo (pode diferir do ano de fabricação) */
    @Column(name = "ano_modelo")
    private Integer anoModelo;

    @Column(length = 40)
    private String cor;

    /** Quilometragem do veículo */
    private Long km;

    /** Número do chassi (VIN) — 17 caracteres alfanuméricos */
    @Column(length = 17, unique = true)
    private String chassi;

    /** RENAVAM — 9 ou 11 dígitos */
    @Column(length = 11)
    private String renavam;

    /** Número do motor */
    @Column(name = "numero_motor", length = 40)
    private String numeroMotor;

    /** Indica se o motor pode ser vendido separadamente */
    @Column(name = "pode_vender_motor", nullable = false)
    @Builder.Default
    private Boolean podeVenderMotor = false;

    /** Indica se o veículo está baixado (sucata/sinistro/desmanche) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean baixado = false;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}
