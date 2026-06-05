package com.flexsys.dto;

import com.flexsys.entity.Notificacao;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacaoDTO {
    private String id;
    private String usuarioId;
    private String titulo;
    private String mensagem;
    private String tipo;
    private Boolean lida;
    private LocalDateTime criadoEm;

    public static NotificacaoDTO from(Notificacao n) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setId(n.getId());
        dto.setUsuarioId(n.getUsuarioId());
        dto.setTitulo(n.getTitulo());
        dto.setMensagem(n.getMensagem());
        dto.setTipo(n.getTipo());
        dto.setLida(n.getLida());
        dto.setCriadoEm(n.getCriadoEm());
        return dto;
    }
}
