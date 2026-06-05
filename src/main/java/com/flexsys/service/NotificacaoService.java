package com.flexsys.service;

import com.flexsys.dto.NotificacaoDTO;
import com.flexsys.entity.Notificacao;
import com.flexsys.repository.NotificacaoRepository;
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

    /** Cria e persiste uma notificação, depois envia via WebSocket ao usuário */
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

        // Usa /topic/notificacoes/{usuarioId} — topic direto por UUID do usuário.
        // Evita confusão com convertAndSendToUser que usa o principal do Spring Security (email).
        try {
            messagingTemplate.convertAndSend("/topic/notificacoes/" + usuarioId, dto);
            log.info("Notificação enviada via WS para usuário {}: {}", usuarioId, titulo);
        } catch (Exception e) {
            log.warn("Falha ao enviar notificação WS para {}: {}", usuarioId, e.getMessage());
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
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        n.setLida(true);
        return NotificacaoDTO.from(repo.save(n));
    }

    @Transactional
    public void marcarTodasComoLidas(String usuarioId) {
        repo.marcarTodasComoLidas(usuarioId);
    }
}
