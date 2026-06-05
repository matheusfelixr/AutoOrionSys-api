package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.PerfilAcesso;
import com.autoorion.service.PerfilAcessoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Perfis de Acesso", description = "Gerenciamento de perfis e permissões de telas")
public class PerfilAcessoController {

    private final PerfilAcessoService perfilAcessoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PerfilAcesso>>> getAll() {
        var perfis = perfilAcessoService.findAll();
        log.info("[Perfis] GET lista: total={}", perfis.size());
        return ResponseEntity.ok(ApiResponse.ok(perfis));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PerfilAcesso>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(perfilAcessoService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PerfilAcesso>> create(@RequestBody PerfilAcesso body) {
        var saved = perfilAcessoService.create(body);
        log.info("[Perfis] POST criado: codigo={}", saved.getCodigo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Perfil criado com sucesso!"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PerfilAcesso>> update(@PathVariable String id,
                                                             @RequestBody PerfilAcesso body) {
        var updated = perfilAcessoService.update(id, body);
        log.info("[Perfis] PUT atualizado: id={}", id);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Perfil atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        perfilAcessoService.delete(id);
        log.info("[Perfis] DELETE: id={}", id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Perfil excluído com sucesso!"));
    }
}
