ackage com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus_grupos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class MenuGrupo extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, length = 80)
    private String nome;
    @Column(length = 10)
    private String icone;
    @Column(nullable = false)
    private Integer ordem = 1;
}
