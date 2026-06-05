ackage com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(
    name = "veiculos",
    indexes = {
        @Index(name = "idx_veiculo_placa",  columnList = "placa"),
        @Index(name = "idx_veiculo_status", columnList = "status"),
        @Index(name = "idx_veiculo_marca",  columnList = "marca"),
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

    @Column(length = 40)
    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVeiculo status;

    @Column(nullable = false)
    private Long km;

    private Double preco;

    @Column(name = "cliente_nome", length = 120)
    private String clienteNome;

    @Column(name = "responsavel_id", length = 36)
    private String responsavelId;

    @Column(name = "responsavel_nome", length = 120)
    private String responsavelNome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    public enum StatusVeiculo { disponivel, reservado, vendido, manutencao }
}
