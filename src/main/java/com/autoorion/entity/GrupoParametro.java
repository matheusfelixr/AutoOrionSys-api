ackage com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grupos_parametro")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class GrupoParametro extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, length = 80)
    private String nome;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Column(nullable = false)
    private Integer ordem = 1;
}
