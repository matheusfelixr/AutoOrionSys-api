ackage com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.entity.TelaSistema;
import com.autoorion.exception.BusinessException;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.TelaSistemaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Telas do Sistema", description = "Cadastro das telas disponÃ­veis no sistema")
public class TelaSistemaController {

    private final TelaSistemaRepository repository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TelaSistema>>> getAll() {
        var telas = repository.findAll();
        log.info("[Telas] GET lista: {} telas", telas.size());
        return ResponseEntity.ok(ApiResponse.list(telas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TelaSistema>> getById(@PathVariable String id) {
        var tela = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tela", id));
        return ResponseEntity.ok(ApiResponse.ok(tela));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TelaSistema>> create(@RequestBody TelaSistema body) {
        if (body.getScreenName() != null && repository.existsByScreenName(body.getScreenName())) {
            throw new BusinessException("Screen name jÃ¡ cadastrado: " + body.getScreenName(), "screenName");
        }
        body.setAtivo(true);
        if (body.getOrdem() == null) body.setOrdem(1);
        var saved = repository.save(body);
        log.info("[Telas] POST criada: screenName={}", saved.getScreenName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(saved, "Tela cadastrada com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TelaSistema>> update(@PathVariable String id,
                                                            @RequestBody TelaSistema body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Tela", id);
        body.setId(id);
        log.info("[Telas] PUT atualizada: id={}", id);
        return ResponseEntity.ok(ApiResponse.ok(repository.save(body), "Tela atualizada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        var tela = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tela", id));
        tela.setAtivo(false);
        repository.save(tela);
        log.info("[Telas] DELETE (soft): id={}", id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Tela excluÃ­da com sucesso!"));
    }
}
