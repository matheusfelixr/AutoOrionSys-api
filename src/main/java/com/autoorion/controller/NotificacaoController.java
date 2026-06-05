package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.dto.NotificacaoDTO;
import com.autoorion.service.NotificacaoService;
import com.autoorion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService service;
    private final UsuarioService usuarioService;

    /** Resolve o UUID do usuÃ¡rio a partir do email (principal do Spring Security) */
    private String resolveUserId(UserDetails userDetails) {
        return usuarioService.findByEmail(userDetails.getUsername()).getId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificacaoDTO>>> listar(
            @AuthenticationPrincipal UserDetails userDetails) {
        var lista = service.listar(resolveUserId(userDetails));
        return ResponseEntity.ok(ApiResponse.ok(lista));
    }

    @GetMapping("/nao-lidas/count")
    public ResponseEntity<ApiResponse<Long>> contarNaoLidas(
            @AuthenticationPrincipal UserDetails userDetails) {
        var count = service.contarNaoLidas(resolveUserId(userDetails));
        return ResponseEntity.ok(ApiResponse.ok(count));
    }

    @PutMapping("/{id}/lida")
    public ResponseEntity<ApiResponse<NotificacaoDTO>> marcarComoLida(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        var dto = service.marcarComoLida(id, resolveUserId(userDetails));
        return ResponseEntity.ok(ApiResponse.ok(dto, "NotificaÃ§Ã£o marcada como lida"));
    }

    @PutMapping("/lidas-todas")
    public ResponseEntity<ApiResponse<Void>> marcarTodasComoLidas(
            @AuthenticationPrincipal UserDetails userDetails) {
        service.marcarTodasComoLidas(resolveUserId(userDetails));
        return ResponseEntity.ok(ApiResponse.ok(null, "Todas as notificaÃ§Ãµes marcadas como lidas"));
    }

    /** Endpoint para testes â€” envia uma notificaÃ§Ã£o para o prÃ³prio usuÃ¡rio logado */
    @PostMapping("/teste")
    public ResponseEntity<ApiResponse<NotificacaoDTO>> enviarTeste(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        var dto = service.enviar(
                resolveUserId(userDetails),   // UUID correto â€” nÃ£o o email
                body.getOrDefault("titulo", "Teste"),
                body.getOrDefault("mensagem", "NotificaÃ§Ã£o de teste"),
                body.getOrDefault("tipo", "info")
        );
        return ResponseEntity.ok(ApiResponse.ok(dto, "NotificaÃ§Ã£o enviada"));
    }
}
