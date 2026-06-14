package com.autoorion.repository;

import com.autoorion.entity.Foto;
import com.autoorion.entity.Foto.TipoMidia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {

    List<Foto> findByEntidadeTipoAndEntidadeIdAndAtivoTrueOrderByOrdemAsc(
            String entidadeTipo, String entidadeId);

    List<Foto> findByEntidadeTipoAndEntidadeIdAndTipoAndAtivoTrueOrderByOrdemAsc(
            String entidadeTipo, String entidadeId, TipoMidia tipo);

    Optional<Foto> findTopByEntidadeTipoAndEntidadeIdAndTipoAndAtivoTrue(
            String entidadeTipo, String entidadeId, TipoMidia tipo);

    void deleteByEntidadeTipoAndEntidadeId(String entidadeTipo, String entidadeId);
}
