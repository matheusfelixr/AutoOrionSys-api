package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.Foto;
import com.autoorion.entity.Foto.TipoMidia;
import com.autoorion.service.FotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Fotos", description = "Fotos e documentos de veículos")
public class FotoController {

    private final FotoService fotoService;

    /**
     * Lista mídias de uma entidade.
     * GET /api/fotos?entidadeTipo=veiculo&entidadeId=abc123
     * GET /api/fotos?entidadeTipo=veiculo&entidadeId=abc123&tipoMidia=FOTO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Foto>>> getByEntidade(
            @RequestParam String entidadeTipo,
            @RequestParam String entidadeId,
            @RequestParam(required = false) TipoMidia tipoMidia) {
        List<Foto> fotos = tipoMidia != null
                ? fotoService.findByEntidadeETipo(entidadeTipo, entidadeId, tipoMidia)
                : fotoService.findByEntidade(entidadeTipo, entidadeId);
        return ResponseEntity.ok(ApiResponse.list(fotos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Foto>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(fotoService.findById(id)));
    }

    /**
     * Salva uma mídia (foto ou documento).
     * Body: { dadosBase64, mimeType, nomeArquivo, tamanhoBytes,
     *         entidadeTipo, entidadeId, tipo, descricao, etapa, ordem }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Foto>> save(@RequestBody Map<String, Object> body) {
        TipoMidia tipo = body.get("tipo") != null
                ? TipoMidia.valueOf((String) body.get("tipo"))
                : TipoMidia.FOTO;

        var foto = Foto.builder()
                .dadosBase64((String) body.get("dadosBase64"))
                .mimeType(body.get("mimeType") != null ? (String) body.get("mimeType") : "image/jpeg")
                .nomeArquivo((String) body.get("nomeArquivo"))
                .tamanhoBytes(body.get("tamanhoBytes") != null
                        ? ((Number) body.get("tamanhoBytes")).longValue() : null)
                .entidadeTipo((String) body.get("entidadeTipo"))
                .entidadeId((String) body.get("entidadeId"))
                .tipo(tipo)
                .descricao((String) body.get("descricao"))
                .etapa((String) body.get("etapa"))
                .ordem(body.get("ordem") != null ? ((Number) body.get("ordem")).intValue() : 0)
                .build();

        var saved = fotoService.save(foto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Mídia salva com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        fotoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Mídia excluída com sucesso!"));
    }
}
