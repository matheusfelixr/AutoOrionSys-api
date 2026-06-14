package com.autoorion.service;

import com.autoorion.entity.Cor;
import com.autoorion.repository.CorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorService {

    private final CorRepository repository;

    public Page<Cor> findAll(String busca, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findFiltered(busca == null ? "" : busca, pageable);
    }

    public List<Cor> findAllAtivas() {
        return repository.findAllAtivas();
    }

    public Cor findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor não encontrada"));
    }

    public Cor create(Cor cor) {
        if (cor.getNome() == null || cor.getNome().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da cor é obrigatório");
        if (repository.existsByNomeIgnoreCase(cor.getNome().trim()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma cor com este nome");
        cor.setNome(cor.getNome().trim());
        return repository.save(cor);
    }

    public Cor update(String id, Cor body) {
        Cor cor = findById(id);
        if (body.getNome() == null || body.getNome().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da cor é obrigatório");
        repository.findByNomeIgnoreCase(body.getNome().trim())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma cor com este nome"); });
        cor.setNome(body.getNome().trim());
        return repository.save(cor);
    }

    public void delete(String id) {
        Cor cor = findById(id);
        cor.setAtivo(false);
        repository.save(cor);
    }
}
