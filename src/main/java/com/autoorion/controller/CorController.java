package com.autoorion.controller;

import com.autoorion.entity.Cor;
import com.autoorion.service.CorService;
import com.autoorion.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cores")
@RequiredArgsConstructor
public class CorController {

    private final CorService service;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listar(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc")  String sortDir) {
        Page<Cor> result = service.findAll(busca, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.page(result));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Cor>>> listarTodas() {
        return ResponseEntity.ok(ApiResponse.ok(service.findAllAtivas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cor>> buscar(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cor>> criar(@RequestBody Cor cor) {
        Cor saved = service.create(cor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Cor cadastrada com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cor>> atualizar(@PathVariable String id, @RequestBody Cor cor) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, cor), "Cor atualizada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String id) {
        service.delete(id);
    }
}
