ackage com.autoorion.repository;
import com.autoorion.entity.GrupoParametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface GrupoParametroRepository extends JpaRepository<GrupoParametro, String> {
    List<GrupoParametro> findByAtivoTrueOrderByOrdemAsc();
}
