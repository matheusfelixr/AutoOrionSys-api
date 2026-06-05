package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.Usuario;
import com.autoorion.service.PermissionsService;
import com.autoorion.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PermissionsService permissionsService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ApiResponse<List<Usuario>>> getAll(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "") String perfil,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        var result = usuarioService.findAll(busca, perfil, status, page, size, sortBy, sortDir);
        log.info("[Usuários] GET lista: total={}", result.getTotalElements());
        return ResponseEntity.ok(ApiResponse.page(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.findById(id)));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<List<String>>> getPermissions(@PathVariable String id) {
        var usuario = usuarioService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(permissionsService.getPermissions(usuario.getPerfil())));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ApiResponse<Usuario>> create(@RequestBody Map<String, Object> body) {
        var usuario = usuarioService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(usuario, "Usuário criado com sucesso!"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<ApiResponse<Usuario>> update(@PathVariable String id,
                                                        @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.update(id, body), "Usuário atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        usuarioService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuário excluído com sucesso!"));
    }
}
