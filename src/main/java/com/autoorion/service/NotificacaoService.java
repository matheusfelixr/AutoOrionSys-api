ackage com.autoorion.service;

import com.autoorion.dto.NotificacaoDTO;
import com.autoorion.entity.Notificacao;
import com.autoorion.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    /** Cria e persiste uma notificaÃ§Ã£o, depois envia via WebSocket ao usuÃ¡rio */
    @Transactional
    public NotificacaoDTO enviar(String usuarioId, String titulo, String mensagem, String tipo) {
        Notificacao notificacao = Notificacao.builder()
                .usuarioId(usuarioId)
                .titulo(titulo)
                .mensagem(mensagem)
                .tipo(tipo)
                .build();

        notificacao = repo.save(notificacao);
        NotificacaoDTO dto = NotificacaoDTO.from(notificacao);

        // Usa /topic/notificacoes/{usuarioId} â€” topic direto por UUID do usuÃ¡rio.
        // Evita confusÃ£o com convertAndSendToUser que usa o principal do Spring Security (email).
        try {
            messagingTemplate.convertAndSend("/topic/notificacoes/" + usuarioId, dto);
            log.info("NotificaÃ§Ã£o enviada via WS para usuÃ¡rio {}: {}", usuarioId, titulo);
        } catch (Exception e) {
            log.warn("Falha ao enviar notificaÃ§Ã£o WS para {}: {}", usuarioId, e.getMessage());
        }

        return dto;
    }

    public List<NotificacaoDTO> listar(String usuarioId) {
        return repo.findByUsuarioIdOrderByCriadoEmDesc(usuarioId)
                .stream().map(NotificacaoDTO::from).toList();
    }

    public long contarNaoLidas(String usuarioId) {
        return repo.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    @Transactional
    public NotificacaoDTO marcarComoLida(String id, String usuarioId) {
        Notificacao n = repo.findById(id)
                .filter(notif -> notif.getUsuarioId().equals(usuarioId))
                .orElseThrow(() -> new RuntimeException("NotificaÃ§Ã£o nÃ£o encontrada"));
        n.setLida(true);
        return NotificacaoDTO.from(repo.save(n));
    }

    @Transactional
    public void marcarTodasComoLidas(String usuarioId) {
        repo.marcarTodasComoLidas(usuarioId);
    }
}
