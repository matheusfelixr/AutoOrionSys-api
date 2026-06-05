ackage com.autoorion.repository;
import com.autoorion.entity.TelaSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface TelaSistemaRepository extends JpaRepository<TelaSistema, String> {
    List<TelaSistema> findByAtivoTrueOrderByMenuIdAscOrdemAsc();
    boolean existsByScreenName(String screenName);
}
