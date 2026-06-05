ackage com.autoorion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Classe base com campos de auditoria automÃ¡ticos.
 * Toda entidade que herdar esta classe terÃ¡:
 *   - criadoEm: preenchido automaticamente na criaÃ§Ã£o
 *   - atualizadoEm: atualizado automaticamente em cada save
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {

    @CreatedDate
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    /** Soft delete â€” false = registro excluÃ­do logicamente */
    @Column(nullable = false)
    private Boolean ativo = true;
}
