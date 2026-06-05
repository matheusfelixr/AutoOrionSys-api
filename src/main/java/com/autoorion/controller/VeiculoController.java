ackage com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.Veiculo;
import com.autoorion.service.VeiculoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "VeÃ­culos", description = "CRUD de veÃ­culos com paginaÃ§Ã£o, filtros e soft delete")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Veiculo>>> getAll(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "modelo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        var result = veiculoService.findAll(busca, status, page, size, sortBy, sortDir);
        log.info("[VeÃ­culos] GET lista: page={}, size={}, total={}", page, size, result.getTotalElements());
        return ResponseEntity.ok(ApiResponse.page(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Veiculo>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(veiculoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Veiculo>> create(@RequestBody Veiculo veiculo) {
        var saved = veiculoService.create(veiculo);
        log.info("[VeÃ­culos] POST criado: placa={}", saved.getPlaca());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "VeÃ­culo cadastrado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Veiculo>> update(@PathVariable String id,
                                                        @RequestBody Veiculo body) {
        return ResponseEntity.ok(ApiResponse.ok(veiculoService.update(id, body), "VeÃ­culo atualizado com sucesso!"));
    }

    /** Soft delete â€” marca como inativo em vez de excluir fisicamente */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        veiculoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "VeÃ­culo excluÃ­do com sucesso!"));
    }
}
