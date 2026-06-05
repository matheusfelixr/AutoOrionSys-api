ackage com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "usuario_id", nullable = false, length = 36)
    private String usuarioId;

    @Column(name = "expira_em", nullable = false)
    private Instant expiraEm;

    @Column(name = "criado_em")
    private Instant criadoEm;
}
