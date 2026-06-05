package com.flexsys.controller;

import com.flexsys.dto.ApiResponse;
import com.flexsys.entity.MenuGrupo;
import com.flexsys.exception.ResourceNotFoundException;
import com.flexsys.repository.MenuGrupoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menus", description = "Seções de agrupamento do menu lateral")
public class MenuGrupoController {

    private final MenuGrupoRepository repository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuGrupo>>> getAll() {
        var menus = repository.findAll();
        log.info("[Menus] GET lista: {} grupos", menus.size());
        return ResponseEntity.ok(ApiResponse.list(menus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuGrupo>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(
            repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", id))
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MenuGrupo>> create(@RequestBody MenuGrupo body) {
        body.setAtivo(true);
        if (body.getOrdem() == null) body.setOrdem(1);
        var saved = repository.save(body);
        log.info("[Menus] POST criado: nome={}", saved.getNome());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Menu criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuGrupo>> update(@PathVariable String id, @RequestBody MenuGrupo body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Menu", id);
        body.setId(id);
        return ResponseEntity.ok(ApiResponse.ok(repository.save(body), "Menu atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Menu", id);
        repository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Menu excluído com sucesso!"));
    }
}
