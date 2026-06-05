package com.autoorion.repository;
import com.autoorion.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {
    List<Foto> findByEntidadeTipoAndEntidadeIdAndAtivoTrue(String tipo, String id);
    void deleteByEntidadeTipoAndEntidadeId(String tipo, String id);
}
