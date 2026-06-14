package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.Marca;
import com.autoorion.service.MarcaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Marcas", description = "CRUD de marcas de veículos")
public class MarcaController {

    private final MarcaService marcaService;

    /** Lista paginada com filtro (para tela de cadastro). */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Marca>>> getAll(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        var result = marcaService.findAll(busca, page, size, sortBy, sortDir);
        log.info("[Marcas] GET lista: page={}, size={}, total={}", page, size, result.getTotalElements());
        return ResponseEntity.ok(ApiResponse.page(result));
    }

    /** Todas as marcas ativas sem paginação — para select/dropdown. */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Marca>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(marcaService.findAllAtivas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Marca>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(marcaService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Marca>> create(@RequestBody Marca marca) {
        var saved = marcaService.create(marca);
        log.info("[Marcas] POST criada: nome={}", saved.getNome());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Marca cadastrada com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Marca>> update(@PathVariable String id,
                                                      @RequestBody Marca body) {
        return ResponseEntity.ok(ApiResponse.ok(marcaService.update(id, body), "Marca atualizada com sucesso!"));
    }

    /** Soft delete */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        marcaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Marca excluída com sucesso!"));
    }
}
