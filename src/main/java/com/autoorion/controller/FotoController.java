package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.Foto;
import com.autoorion.service.FotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Fotos", description = "Armazenamento de fotos no banco de dados")
public class FotoController {

    private final FotoService fotoService;

    /** Lista fotos de uma entidade: GET /api/fotos?tipo=veiculo&entidadeId=abc123 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Foto>>> getByEntidade(
            @RequestParam String tipo,
            @RequestParam String entidadeId) {
        var fotos = fotoService.findByEntidade(tipo, entidadeId);
        return ResponseEntity.ok(ApiResponse.list(fotos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Foto>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(fotoService.findById(id)));
    }

    /**
     * Salva uma foto.
     * Body: { dadosBase64, mimeType, nomeArquivo, tamanhoBytes, entidadeTipo, entidadeId, descricao, etapa }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Foto>> save(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails userDetails) {

        var foto = Foto.builder()
                .dadosBase64((String) body.get("dadosBase64"))
                .mimeType((String) body.getOrDefault("mimeType", "image/jpeg"))
                .nomeArquivo((String) body.get("nomeArquivo"))
                .tamanhoBytes(body.get("tamanhoBytes") != null
                        ? ((Number) body.get("tamanhoBytes")).longValue() : null)
                .entidadeTipo((String) body.get("entidadeTipo"))
                .entidadeId((String) body.get("entidadeId"))
                .descricao((String) body.get("descricao"))
                .etapa((String) body.get("etapa"))
                .build();

        String userId = userDetails != null ? userDetails.getUsername() : null;
        var saved = fotoService.save(foto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Foto salva com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        fotoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Foto excluída com sucesso!"));
    }
}
