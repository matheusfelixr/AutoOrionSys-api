package com.autoorion.repository;

import com.autoorion.entity.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, String> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Marca> findByNomeIgnoreCase(String nome);

    @Query("""
        SELECT m FROM Marca m
        WHERE m.ativo = true
          AND (:busca IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :busca, '%')))
        ORDER BY m.nome ASC
        """)
    Page<Marca> findFiltered(@Param("busca") String busca, Pageable pageable);

    @Query("SELECT m FROM Marca m WHERE m.ativo = true ORDER BY m.nome ASC")
    List<Marca> findAllAtivas();
}
