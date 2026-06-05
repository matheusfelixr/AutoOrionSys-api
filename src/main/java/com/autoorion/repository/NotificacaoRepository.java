ackage com.autoorion.repository;

import com.autoorion.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {
    List<Notificacao> findByUsuarioIdOrderByCriadoEmDesc(String usuarioId);
    long countByUsuarioIdAndLidaFalse(String usuarioId);

    @Modifying
    @Transactional
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.usuarioId = :usuarioId")
    void marcarTodasComoLidas(String usuarioId);
}
