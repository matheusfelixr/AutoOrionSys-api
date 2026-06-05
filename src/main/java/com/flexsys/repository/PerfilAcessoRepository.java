package com.flexsys.repository;
import com.flexsys.entity.PerfilAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface PerfilAcessoRepository extends JpaRepository<PerfilAcesso, String> {
    Optional<PerfilAcesso> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
