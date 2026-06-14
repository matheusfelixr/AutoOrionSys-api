package com.autoorion.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.autoorion.entity.PerfilAcesso;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.PerfilAcessoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfilAcessoService {

    private final PerfilAcessoRepository repository;
    private final ObjectMapper objectMapper;

    public List<PerfilAcesso> findAll() {
        return repository.findAll();
    }

    public PerfilAcesso findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));
    }

    public PerfilAcesso findByCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", codigo));
    }

    /**
     * Retorna a lista de screenNames permitidos para um código de perfil.
     * Lê do banco de dados — substituição da lógica hardcoded.
     */
    public List<String> getScreensForPerfil(String codigoPerfil) {
        return repository.findByCodigo(codigoPerfil)
                .map(p -> parseScreens(p.getTelasPermitidas()))
                .orElse(List.of("dashboard", "perfil"));
    }

    public PerfilAcesso create(PerfilAcesso body) {
        body.setAtivo(true);
        return repository.save(body);
    }

    public PerfilAcesso update(String id, PerfilAcesso body) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Perfil", id);
        body.setId(id);
        return repository.save(body);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Perfil", id);
        repository.deleteById(id);
    }

    /**
     * Retorna as permissões granulares por tela para um código de perfil.
     * Ex: {"veiculos":["ver","criar","editar","excluir"],"marcas":["ver"]}
     */
    public Map<String, List<String>> getActionsForPerfil(String codigoPerfil) {
        return repository.findByCodigo(codigoPerfil)
                .map(p -> parseActions(p.getPermissoes()))
                .orElse(Map.of());
    }

    private List<String> parseScreens(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("[PerfilAcessoService] Erro ao parsear telas: {}", json);
            return List.of();
        }
    }

    private Map<String, List<String>> parseActions(String json) {
        if (json == null || json.isBlank()) return Map.of();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
        } catch (Exception e) {
            log.warn("[PerfilAcessoService] Erro ao parsear permissoes: {}", json);
            return Map.of();
        }
    }
}
