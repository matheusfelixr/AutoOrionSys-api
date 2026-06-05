package com.autoorion.repository;
import com.autoorion.entity.MenuGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MenuGrupoRepository extends JpaRepository<MenuGrupo, String> {
    List<MenuGrupo> findByAtivoTrueOrderByOrdemAsc();
}
