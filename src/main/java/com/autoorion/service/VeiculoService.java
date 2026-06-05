ackage com.autoorion.service;

import com.autoorion.entity.Veiculo;
import com.autoorion.exception.BusinessException;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.UsuarioRepository;
import com.autoorion.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VeiculoService {

    private final VeiculoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public Page<Veiculo> findAll(String busca, String status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findFiltered(
            busca.isBlank() ? null : busca,
            status.isBlank() ? null : status,
            pageable
        );
    }

    public Veiculo findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VeÃ­culo", id));
    }

    public Veiculo create(Veiculo veiculo) {
        if (veiculo.getPlaca() != null && repository.existsByPlaca(veiculo.getPlaca())) {
            throw new BusinessException("Placa jÃ¡ cadastrada no sistema.", "placa");
        }
        veiculo.setAtivo(true);
        if (veiculo.getKm() == null) veiculo.setKm(0L);
        var saved = repository.save(veiculo);
        log.info("[VeiculoService] Criado: placa={}", saved.getPlaca());
        // Notifica todos os admins ativos
        usuarioRepository.findByPerfilAtivo("admin").forEach(admin -> {
            notificacaoService.enviar(
                admin.getId(),
                "Novo veÃ­culo cadastrado",
                "O veÃ­culo " + saved.getModelo() + " (" + saved.getPlaca() + ") foi cadastrado.",
                "info"
            );
        });
        return saved;
    }

    public Veiculo update(String id, Veiculo body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("VeÃ­culo", id);
        body.setId(id);
        body.setAtivo(true);
        log.info("[VeiculoService] Atualizado: id={}", id);
        return repository.save(body);
    }

    public void delete(String id) {
        var veiculo = findById(id);
        veiculo.setAtivo(false); // soft delete
        repository.save(veiculo);
        log.info("[VeiculoService] ExcluÃ­do (soft): id={}", id);
    }
}
