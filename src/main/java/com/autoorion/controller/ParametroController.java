package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.GrupoParametro;
import com.autoorion.entity.Parametro;
import com.autoorion.exception.BusinessException;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.GrupoParametroRepository;
import com.autoorion.repository.ParametroRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Parâmetros", description = "Configurações parametrizáveis do sistema")
public class ParametroController {

    private final ParametroRepository repository;
    private final GrupoParametroRepository grupoRepository;

    // ── Parâmetros ────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<Parametro>>> getAll() {
        var list = repository.findAll();
        log.info("[Parâmetros] GET lista: {} parâmetros", list.size());
        return ResponseEntity.ok(ApiResponse.list(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Parametro>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(
            repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Parâmetro", id))
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Parametro>> create(@RequestBody Parametro body) {
        if (body.getNome() != null && repository.existsByNome(body.getNome())) {
            throw new BusinessException("Nome de parâmetro já cadastrado: " + body.getNome(), "nome");
        }
        body.setAtivo(true);
        if (body.getOrdem() == null) body.setOrdem(1);
        var saved = repository.save(body);
        log.info("[Parâmetros] POST criado: nome={}", saved.getNome());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Parâmetro criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Parametro>> update(@PathVariable String id, @RequestBody Parametro body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Parâmetro", id);
        body.setId(id);
        log.info("[Parâmetros] PUT atualizado: id={}", id);
        return ResponseEntity.ok(ApiResponse.ok(repository.save(body), "Parâmetro atualizado com sucesso!"));
    }

    /** Atualiza apenas o valor de um parâmetro */
    @PatchMapping("/{id}/valor")
    public ResponseEntity<ApiResponse<Parametro>> updateValor(@PathVariable String id,
                                                               @RequestBody Map<String, String> body) {
        var param = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetro", id));
        param.setValor(body.get("valor"));
        log.info("[Parâmetros] PATCH valor: nome={}, valor={}", param.getNome(), param.getValor());
        return ResponseEntity.ok(ApiResponse.ok(repository.save(param), "Valor atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Parâmetro", id);
        repository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Parâmetro excluído com sucesso!"));
    }

    // ── Grupos de Parâmetros ─────────────────────────────────────────────────

    @GetMapping("/grupos")
    public ResponseEntity<ApiResponse<List<GrupoParametro>>> getGrupos() {
        return ResponseEntity.ok(ApiResponse.list(grupoRepository.findAll()));
    }

    @PostMapping("/grupos")
    public ResponseEntity<ApiResponse<GrupoParametro>> createGrupo(@RequestBody GrupoParametro body) {
        body.setAtivo(true);
        if (body.getOrdem() == null) body.setOrdem(1);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(grupoRepository.save(body), "Grupo criado com sucesso!"));
    }

    @PutMapping("/grupos/{id}")
    public ResponseEntity<ApiResponse<GrupoParametro>> updateGrupo(@PathVariable String id,
                                                                     @RequestBody GrupoParametro body) {
        if (!grupoRepository.existsById(id)) throw new ResourceNotFoundException("Grupo", id);
        body.setId(id);
        return ResponseEntity.ok(ApiResponse.ok(grupoRepository.save(body), "Grupo atualizado com sucesso!"));
    }

    @DeleteMapping("/grupos/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGrupo(@PathVariable String id) {
        if (!grupoRepository.existsById(id)) throw new ResourceNotFoundException("Grupo", id);
        grupoRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Grupo excluído com sucesso!"));
    }
}
