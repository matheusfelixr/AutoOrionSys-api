package com.autoorion.repository;

import com.autoorion.entity.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String> {

    boolean existsByPlaca(String placa);

    @Query("""
        SELECT v FROM Veiculo v
        WHERE v.ativo = true
          AND (
              :busca IS NULL
              OR LOWER(v.modelo)  LIKE LOWER(CONCAT('%', :busca, '%'))
              OR LOWER(v.marca)   LIKE LOWER(CONCAT('%', :busca, '%'))
              OR LOWER(v.placa)   LIKE LOWER(CONCAT('%', :busca, '%'))
              OR LOWER(v.chassi)  LIKE LOWER(CONCAT('%', :busca, '%'))
              OR LOWER(v.renavam) LIKE LOWER(CONCAT('%', :busca, '%'))
          )
        """)
    Page<Veiculo> findFiltered(
        @Param("busca") String busca,
        Pageable pageable
    );
}
