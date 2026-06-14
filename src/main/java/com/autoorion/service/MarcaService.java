package com.autoorion.service;

import com.autoorion.entity.Marca;
import com.autoorion.exception.BusinessException;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarcaService {

    private final MarcaRepository repository;

    public Page<Marca> findAll(String busca, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findFiltered(
            busca == null || busca.isBlank() ? null : busca,
            pageable
        );
    }

    /** Retorna todas as marcas ativas sem paginação — para select/dropdown. */
    public List<Marca> findAllAtivas() {
        return repository.findAllAtivas();
    }

    public Marca findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", id));
    }

    public Marca create(Marca marca) {
        String nome = marca.getNome() == null ? "" : marca.getNome().trim();
        if (nome.isEmpty()) throw new BusinessException("Nome da marca é obrigatório.", "nome");
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Já existe uma marca com este nome.", "nome");
        }
        marca.setNome(nome);
        marca.setAtivo(true);
        var saved = repository.save(marca);
        log.info("[MarcaService] Criada: nome={}", saved.getNome());
        return saved;
    }

    public Marca update(String id, Marca body) {
        Marca existing = findById(id);
        String nome = body.getNome() == null ? "" : body.getNome().trim();
        if (nome.isEmpty()) throw new BusinessException("Nome da marca é obrigatório.", "nome");

        // Verifica duplicidade excluindo a própria marca
        repository.findByNomeIgnoreCase(nome).ifPresent(found -> {
            if (!found.getId().equals(id)) {
                throw new BusinessException("Já existe uma marca com este nome.", "nome");
            }
        });

        existing.setNome(nome);
        log.info("[MarcaService] Atualizada: id={}, nome={}", id, nome);
        return repository.save(existing);
    }

    public void delete(String id) {
        Marca marca = findById(id);
        marca.setAtivo(false);
        repository.save(marca);
        log.info("[MarcaService] Excluída (soft): id={}", id);
    }
}
