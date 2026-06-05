package com.flexsys.repository;
import com.flexsys.entity.MenuGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MenuGrupoRepository extends JpaRepository<MenuGrupo, String> {
    List<MenuGrupo> findByAtivoTrueOrderByOrdemAsc();
}
