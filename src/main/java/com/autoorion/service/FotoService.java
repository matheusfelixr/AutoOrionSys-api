ackage com.autoorion.service;

import com.autoorion.entity.Foto;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.FotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FotoService {

    private final FotoRepository repository;

    public List<Foto> findByEntidade(String tipo, String entidadeId) {
        return repository.findByEntidadeTipoAndEntidadeIdAndAtivoTrue(tipo, entidadeId);
    }

    public Foto findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
    }

    public Foto save(Foto foto, String usuarioId) {
        foto.setAtivo(true);
        foto.setCriadoPor(usuarioId);
        var saved = repository.save(foto);
        log.info("[FotoService] Salva: id={}, entidade={}/{}, tamanho={}bytes",
                saved.getId(), foto.getEntidadeTipo(), foto.getEntidadeId(), foto.getTamanhoBytes());
        return saved;
    }

    public void delete(String id) {
        var foto = findById(id);
        foto.setAtivo(false);
        repository.save(foto);
        log.info("[FotoService] ExcluÃ­da (soft): id={}", id);
    }
}
