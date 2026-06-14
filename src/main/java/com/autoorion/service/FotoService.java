package com.autoorion.service;

import com.autoorion.entity.Foto;
import com.autoorion.entity.Foto.TipoMidia;
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

    public List<Foto> findByEntidade(String entidadeTipo, String entidadeId) {
        return repository.findByEntidadeTipoAndEntidadeIdAndAtivoTrueOrderByOrdemAsc(entidadeTipo, entidadeId);
    }

    public List<Foto> findByEntidadeETipo(String entidadeTipo, String entidadeId, TipoMidia tipo) {
        return repository.findByEntidadeTipoAndEntidadeIdAndTipoAndAtivoTrueOrderByOrdemAsc(entidadeTipo, entidadeId, tipo);
    }

    public Foto findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Foto", id));
    }

    public Foto save(Foto foto) {
        foto.setAtivo(true);
        // FOTO_REDE_SOCIAL: apenas uma por veículo — desativa a anterior
        if (TipoMidia.FOTO_REDE_SOCIAL.equals(foto.getTipo())) {
            repository.findTopByEntidadeTipoAndEntidadeIdAndTipoAndAtivoTrue(
                    foto.getEntidadeTipo(), foto.getEntidadeId(), TipoMidia.FOTO_REDE_SOCIAL)
                .ifPresent(antiga -> {
                    antiga.setAtivo(false);
                    repository.save(antiga);
                });
        }
        var saved = repository.save(foto);
        log.info("[FotoService] Salva: id={}, tipo={}, entidade={}/{}, tamanho={}bytes",
                saved.getId(), saved.getTipo(), foto.getEntidadeTipo(), foto.getEntidadeId(), foto.getTamanhoBytes());
        return saved;
    }

    public void delete(String id) {
        var foto = findById(id);
        foto.setAtivo(false);
        repository.save(foto);
        log.info("[FotoService] Excluída (soft): id={}", id);
    }
}
