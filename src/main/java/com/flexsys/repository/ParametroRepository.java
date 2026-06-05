package com.flexsys.repository;
import com.flexsys.entity.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ParametroRepository extends JpaRepository<Parametro, String> {
    List<Parametro> findByGrupoIdAndAtivoTrueOrderByOrdemAsc(String grupoId);
    boolean existsByNome(String nome);
}
