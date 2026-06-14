package com.autoorion.repository;

import com.autoorion.entity.Cor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CorRepository extends JpaRepository<Cor, String> {

    @Query("SELECT c FROM Cor c WHERE c.ativo = true AND " +
           "(:busca IS NULL OR :busca = '' OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%')))")
    Page<Cor> findFiltered(@Param("busca") String busca, Pageable pageable);

    @Query("SELECT c FROM Cor c WHERE c.ativo = true ORDER BY c.nome ASC")
    List<Cor> findAllAtivas();

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Cor> findByNomeIgnoreCase(String nome);
}
